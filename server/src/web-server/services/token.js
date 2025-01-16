const crypto = require('crypto');

/**
 * Service for generating authentication tokens
 */
exports.generateToken = () => {
  return crypto.randomBytes(16).toString('hex');
};