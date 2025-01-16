const Movie = require('../models/movies');
const Category = require('../models/categories');


const createMovie = async (name,
    description, duration, releaseYear, actors, categoryNames,
    ageAllow, director, language, imageUrl, trailerUrl) => {
    try {
        // Input validation
        const movies = await Movie.find({ name: name })
        if (movies.length > 0) {
            return { success: false, error: "name movie already exist!" }
        }
        if (!name || !description || !duration || !releaseYear ||
            !ageAllow || !director || !language) {
            return { success: false, error: "Missing required fields" }
        }
        if (!categoryNames || !Array.isArray(categoryNames) || categoryNames.length === 0) {
            return { success: false, error: "At least one category is required" }
        }

        const categories = await Category.find({ name: { $in: categoryNames } });
        if (categories.length !== categoryNames.length) {
            return { success: false, error: "One or more invalid category names" }
        }

        const movie = new Movie({
            name: name,
            description: description,
            duration: duration,
            releaseYear: releaseYear,
            actors: actors || [],
            categories: categoryNames,
            ageAllow: ageAllow,
            director: director,
            language: language,
            imageUrl: imageUrl,
            trailerUrl: trailerUrl
        })

        const savedMovie = await movie.save()
        if (!savedMovie) {
            return { success: false, error: "Could not create movie" }
        }

        // Update categories with movie name
        for (const category of categories) {
            await Category.findByIdAndUpdate(
                category._id,
                {
                    $push: { movies: savedMovie.name },
                    $inc: { movieCount: 1 }
                },
                { new: true }
            )
        }

        return { success: true, data: savedMovie }
    } catch (error) {
        return { success: false, error: error.message }
    }
}

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
        // Validate movie exists
        const findMovie = await Movie.findById(id)
        if (!findMovie) {
            return { success: false, error: "Movie not found" }
        }

        // Input validation
        if (!movieData.name || !movieData.description || !movieData.duration ||
            !movieData.releaseYear || !movieData.ageAllow ||
            !movieData.director || !movieData.language) {
            return { success: false, error: "Missing required fields" }
        }

        // Category validation
        if (!movieData.categories || !Array.isArray(movieData.categories) ||
            movieData.categories.length === 0) {
            return { success: false, error: "At least one category is required" }
        }

        // Validate categories exist by name
        const newCategories = await Category.find({ name: { $in: movieData.categories } });
        if (newCategories.length !== movieData.categories.length) {
            return { success: false, error: "One or more invalid category names" }
        }

        // Get old categories by name
        const oldCategories = await Category.find({ name: { $in: findMovie.categories } });

        // Remove movie from old categories
        for (const category of oldCategories) {
            await Category.findByIdAndUpdate(
                category._id,
                {
                    $pull: { movies: findMovie.name },
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
        )

        // Add movie to new categories
        for (const category of newCategories) {
            await Category.findByIdAndUpdate(
                category._id,
                {
                    $push: { movies: movieData.name },
                    $inc: { movieCount: 1 }
                },
                { new: true }
            );
        }

        return { success: true, data: updatedMovie };
    } catch (error) {
        return { success: false, error: error.message }
    }
}

const deleteMovie = async (id) => {
    try {
        const movie = await Movie.findById(id)
        if (!movie) {
            return { error: "Movie not found" }
        }


        // Get categories by name and remove the movie
        const categories = await Category.find({ name: { $in: movie.categories } });
        for (const category of categories) {
            await Category.findByIdAndUpdate(
                category._id,
                {
                    $pull: { movies: movie.name },
                    $inc: { movieCount: -1 }
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
        const moviesUserId = moviesUser.map(movie => movie._id);

        const results = [];

        for (const category of categories) {
            const moviefit = await Movie.aggregate([{
                $match: {
                    categories: category._id,
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
}
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