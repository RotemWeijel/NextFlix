const mongoose = require('mongoose');

/**
 * User Schema - Defines the structure for storing user data
 * @property {String} username - Unique identifier for the user
 * @property {String} password - Hashed password
 * @property {String} full_name - User's full name
 * @property {String} picture - URL to user's profile picture
 * @property {Boolean} isAdmin - Administrative permissions flag
 * @property {Array} watchedMovies - Array of watched movie IDs
 */
const UserSchema = new mongoose.Schema({
  username: { type: String, required: true, unique: true },
  password: { type: String, required: true },
  full_name: { type: String, required: true },
  picture: { type: String },
  isAdmin: { 
    type: Boolean, 
    default: false 
  },
  watchedMovies: [{
    type: mongoose.Schema.Types.ObjectId,
    ref: 'Movie'
  }]
});

module.exports = mongoose.model('User', UserSchema);