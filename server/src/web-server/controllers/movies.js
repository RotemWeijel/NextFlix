const movieController = require('../services/movies')
const Movie = require('../models/movies')
const RecommendationClient = require('../services/recommend');
const recommendationClient = new RecommendationClient(
    process.env.RECOMMENDATION_SERVER_HOST || 'localhost',
    process.env.RECOMMENDATION_SERVER_PORT || 6000
);


const createMovie = async (req, res) => {
    try {

        const result = await movieController.createMovie(
            req.body.name,
            req.body.description,
            req.body.duration,
            req.body.releaseYear,
            req.body.actors,
            req.body.categories,
            req.body.ageAllow,
            req.body.director,
            req.body.language,
            req.body.imageUrl,
            req.body.trailerUrl
        );

        if (result.error) {
            return res.status(400).json({ error: result.error });
        }

        return res.status(201)
            .location(`/api/movies/${result.data._id}`)
            .json(result.data);
    } catch (error) {
        return res.status(500).json({ error: 'Internal server error' });
    }
};

const getMovieById = async (req, res) => {
    const result = await movieController.getMoviebyId(req.params.id)
    if (result.error) {
        return res.status(404).json({ error: result.error })
    }
    return res.status(200).json(result.data)
};

const findMovieByQuery = async (req, res) => {
    const result = await movieController.findMovieByQuery(req.params.query)
    if (result.error) {
        return res.status(404).json({ error: result.error })
    }
    return res.status(200).json(result.data)
};

const putMovie = async (req, res) => {
    const id = req.params.id
    const result = await movieController.putMovie(id, req.body)
    if (result.error) {
        return res.status(404).json({ error: result.error })
    }
    return res.status(204).send()
};

const deleteMovie = async (req, res) => {
    const id = req.params.id
    const result = await movieController.deleteMovie(id)
    if (result.error) {
        return res.status(404).json({ error: result.error })
    }
    return res.status(204).send()
};

const getMovies = async (req, res) => {
    const id = req.params.id
    const result = await movieController.getMovies(id)
    if (result.error) {
        return res.status(404).json({ error: result.error })
    }
    return res.status(200).json(result.data)
};

const getRecommendations = async (req, res) => {
    try {
        const result = await recommendationClient.getRecommendations(
            req.userId,
            req.params.id
        );

        if (!result.success) {
            return res.status(400).json({ error: result.error });
        }

        // If we have recommendation IDs, fetch the full movie details
        if (result.data && result.data.length > 0) {
            const movies = await Movie.find({ _id: { $in: result.data } });
            return res.status(200).json(movies);
        }
    

        // If no recommendations
        return res.status(200).json([]);
    } catch (error) {
        console.error('Error getting recommendations:', error);
        return res.status(500).json({ error: error.message });
    }
};

const addRecommendation = async (req, res) => {
    const result = await recommendationClient.addWatchedMovie(
        req.userId,  // From auth middleware
        req.params.id
    );

    if (!result.success) {
        return res.status(400).json({ error: result.error });
    }

    return res.status(201).send();
};

module.exports = { createMovie, getMovieById, findMovieByQuery, putMovie, deleteMovie, getMovies, getRecommendations, addRecommendation }