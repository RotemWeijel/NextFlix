const mongoose = require('mongoose')
const moviesScheme = new mongoose.Schema({
    name: {
        type: String,
        require: true,
        trim: true
    },
    description: {
        type: String,
        require: true,
        trim: true
    },
    duration: {
        type: Number,
        require: true,
        min: 1
    },
    releaseYear: {
        type: Number,
        require: true,
    },
    addedAt: {
        type: Date,
        default: Date.now
    },
    actors: [{
        name: {
            type: String,
            require: true,
            trim: true
        }
    }],
    categories: [{
        type: String,
        require: true,
        trim: true
    }],
    watchedBy: [{
        userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User' },
        watchedAt: { type: Date, default: Date.now }
    }],
    ageAllow: {
        type: Number,
        require: true
    },
    director: {
        type: String,
        require: true,
        trim: true
    },
    language: {
        type: String,
        require: true,
        trim: true
    },
    imageUrl: {
        type: String,
        trim: true
    },
    trailerUrl: {
        type: String,
        trim: true
    },
    videoUrl: {
        type: String,
        require: true,
        trim: true
    }

})
moviesScheme.index({
    name: 'text',
    description: 'text',
    director: 'text',
    language: 'text',
    'actors.name': 'text'
});
const Movie = mongoose.model('Movie', moviesScheme)
module.exports = Movie