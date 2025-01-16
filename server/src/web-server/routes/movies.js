const movieControllers = require('../controllers/movies')
const verifyAuth = require('../middleware/authMiddleware')
const express = require('express');
var router = express.Router();

router.route('/')
    .get(verifyAuth, movieControllers.getMovies)
    .post(verifyAuth, movieControllers.createMovie)
router.route('/search/:query')
    .get(verifyAuth, movieControllers.findMovieByQuery)
router.route('/:id')
    .get(verifyAuth, movieControllers.getMovieById)
    .put(verifyAuth, movieControllers.putMovie)
    .delete(verifyAuth, movieControllers.deleteMovie)
router.route('/:id/recommend')
    .get(verifyAuth, movieControllers.getRecommendations)
    .post(verifyAuth, movieControllers.addRecommendation)

module.exports = router;