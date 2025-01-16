const crypto = require('crypto');

/**
 * Service for handling password-related operations
 */
exports.hashPassword = (password) => {
  return crypto.createHash('sha256').update(password).digest('hex');
};

exports.verifyPassword = (inputPassword, hashedPassword) => {
  const hashedInput = crypto.createHash('sha256').update(inputPassword).digest('hex');
  return hashedInput === hashedPassword;
};
