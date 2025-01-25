const Movie = require('../models/movies');
const Category = require('../models/categories');


const createMovie = async (name,
    description, duration, releaseYear, actors, categoryIds, 
    ageAllow, director, language, imageUrl, trailerUrl, videoUrl) => {
    try {
        // Input validation
        const movies = await Movie.find({ name: name });
        if (movies.length > 0) {
            return { success: false, error: "name movie already exist!" };
        }
        if (!name || !description || !duration || !releaseYear ||
            !ageAllow || !director || !language || !videoUrl) {
            return { success: false, error: "Missing required fields" };
        }
        if (!categoryIds || !Array.isArray(categoryIds) || categoryIds.length === 0) {
            return { success: false, error: "At least one category is required" };
        }

        // Validate categories by ID
        const categories = await Category.find({ _id: { $in: categoryIds } });
        if (categories.length !== categoryIds.length) {
            return { success: false, error: "One or more invalid category IDs" };
        }

        const movie = new Movie({
            name: name,
            description: description,
            duration: duration,
            releaseYear: releaseYear,
            actors: actors || [],
            categories: categoryIds, 
            ageAllow: ageAllow,
            director: director,
            language: language,
            imageUrl: imageUrl,
            trailerUrl: trailerUrl,
            videoUrl: videoUrl
        });

        const savedMovie = await movie.save();
        if (!savedMovie) {
            return { success: false, error: "Could not create movie" };
        }

        // Update categories with movie ID
        for (const category of categories) {
            await Category.findByIdAndUpdate(
                category._id,
                {
                    $push: { movies: savedMovie._id },
                    $inc: { movieCount: 1 }
                },
                { new: true }
            );
        }

        return { success: true, data: savedMovie };
    } catch (error) {
        return { success: false, error: error.message };
    }
};

const getMoviebyId = async (id) => {
    try {
        const movie = await Movie.findById(id)
        if (!movie) {
            return { success: false, error: 'Movie not found' }
        }
        return { success: true, data: movie }
    } catch (error) {
        return { success: false, error: 'Error fetching movie' }
    }
}

const putMovie = async (id, movieData) => {
    try {
        console.log('Received Movie Update Request:', { id, movieData });

        // Validate movie exists
        const findMovie = await Movie.findById(id);
        if (!findMovie) {
            console.error(`Movie not found with ID: ${id}`);
            return { success: false, error: "Movie not found" };
        }

        // Detailed input validation with specific error messages
        const requiredFields = [
            'name', 'description', 'duration', 
            'releaseYear', 'ageAllow', 'director', 'language'
        ];

        for (const field of requiredFields) {
            if (!movieData[field]) {
                console.error(`Missing required field: ${field}`);
                return { 
                    success: false, 
                    error: `Missing required field: ${field}` 
                };
            }
        }

        // Category validation with detailed logging
        if (!movieData.categories || !Array.isArray(movieData.categories)) {
            console.error('Invalid categories format');
            return { success: false, error: "Categories must be an array" };
        }

        if (movieData.categories.length === 0) {
            console.error('No categories provided');
            return { success: false, error: "At least one category is required" };
        }

        // Validate categories exist by ID
        const newCategories = await Category.find({ _id: { $in: movieData.categories } });
        
        console.log('Found Categories:', newCategories.map(c => c._id));
        console.log('Requested Category IDs:', movieData.categories);

        if (newCategories.length !== movieData.categories.length) {
            console.error('Invalid category IDs detected');
            return { success: false, error: "One or more invalid category IDs" };
        }

        // Remove movie from old categories
        for (const category of newCategories) {
            await Category.findByIdAndUpdate(
                category._id,
                {
                    $pull: { movies: findMovie._id },
                    $inc: { movieCount: -1 }
                },
                { new: true }
            );
        }

        // Update the movie
        const updatedMovie = await Movie.findByIdAndUpdate(
            id,
            {
                name: movieData.name,
                description: movieData.description,
                duration: movieData.duration,
                releaseYear: movieData.releaseYear,
                actors: movieData.actors || [],
                categories: movieData.categories,
                ageAllow: movieData.ageAllow,
                director: movieData.director,
                language: movieData.language,
                imageUrl: movieData.imageUrl,
                trailerUrl: movieData.trailerUrl
            },
            { new: true, runValidators: true }
        );

        // Add movie to new categories
        for (const category of newCategories) {
            await Category.findByIdAndUpdate(
                category._id,
                {
                    $push: { movies: updatedMovie._id }, 
                    $inc: { movieCount: 1 }
                },
                { new: true }
            );
        }

        console.log('Movie successfully updated:', updatedMovie);
        return { success: true, data: updatedMovie };

    } catch (error) {
        console.error('Detailed Error in putMovie:', {
            message: error.message,
            stack: error.stack,
            movieData: movieData
        });
        return { success: false, error: error.message };
    }
};

const deleteMovie = async (id) => {
    try {
        const movie = await Movie.findById(id)
        if (!movie) {
            return { error: "Movie not found" }
        }


        // Get categories by name and remove the movie
        const categories = await Category.find({ movies: movie._id });
        for (const category of categories) {
            await Category.findByIdAndUpdate(
                category._id,
                {
                    $pull: { movies: movie._id }, // Remove movie ObjectId
                    $inc: { movieCount: -1 } // Decrement the movie count
                },
                { new: true }
            );
        }

        // Delete the movie        
        await movie.deleteOne()
        return { data: null }
    }
    catch (error) {
        return { error: error.message }
    }
}
const findMovieByQuery = async (query) => {
    //match all movies that each of the fields contains the query
    //pay attention to search wuery on fields with nums
    const matchingMovies = await Movie.find({
        $or: [
            { name: { $regex: query, $options: 'i' } },
            { description: { $regex: query, $options: 'i' } },
            { director: { $regex: query, $options: 'i' } },
            { language: { $regex: query, $options: 'i' } },
            { 'actors.name': { $regex: query, $options: 'i' } }  // Fixed actors search
        ]
    });

    return matchingMovies.length ? { data: matchingMovies } : { error: 'Movies not found' };
}

const getMovies = async (userId) => {
    try {
        const categories = await Category.find({ promoted: true });
        const moviesUser = await getMoviesByUser(userId);
        const moviesUserId = moviesUser.map(movie => movie._id); // Changed from name to _id

        const results = [];

        for (const category of categories) {
            const moviefit = await Movie.aggregate([{
                $match: {
                    categories: category._id, // Changed from category.name
                    _id: { $nin: moviesUserId }
                }
            }, { $sample: { size: 20 } }]);

            if (moviefit.length > 0) {
                results.push({
                    categoryId: category._id,
                    categoryName: category.name,
                    promoted: true,
                    movies: moviefit
                });
            }
        }

        if (moviesUser.length > 0) {
            results.push({
                categoryId: 'watched',
                categoryName: 'Watched Movies',
                promoted: false,
                movies: moviesUser.slice(0, 20).sort(() => Math.random() - 0.5)
            });
        }

        return { data: results };
    } catch (error) {
        return { error: error.message };
    }
};

const getMoviesByUser = async (userId) => {
    try {
        const watchedMovies = await Movie.find({
            'watchedBy.userId': userId
        })
            .sort({ 'watchedBy.watchedAt': -1 });
        return watchedMovies
    }
    catch (error) {
        return { error: error.message }
    }
}
module.exports = { createMovie, getMoviebyId, putMovie, deleteMovie, findMovieByQuery, getMovies }