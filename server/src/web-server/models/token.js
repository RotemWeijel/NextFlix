const mongoose = require('mongoose');

/**
 * Token Schema - Stores authentication tokens
 * @property {ObjectId} userId - Reference to the User model
 * @property {String} token - Authentication token string
 */
const TokenSchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
  token: { type: String, required: true }
});

module.exports = mongoose.model('Token', TokenSchema);