const mongoose = require('mongoose')
const Movie = require('../models/movies')
const categorySchema = new mongoose.Schema({
    name: {
        type: String,
        required: true,
        trim: true,
        unique: true
    },
    promoted: {
        type: Boolean,
        default: false
    },
    description: {
        type: String,
        default: '',
        trim: true
    },
    sortOrder: {
        type: Number,
        default: 0
    },
    movies: [{
        type: String,
        default: null
    }],
    parentCategory: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Category',
        default: null
    },
    movieCount: {
        type: Number,
        default: 0
    },
    displayInMenu: {
        type: Boolean,
        default: true
    }
}, {
    timestamps: true
});
const Category = mongoose.model('Category', categorySchema)
module.exports = Category;