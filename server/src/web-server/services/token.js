const jwt = require('jsonwebtoken');

const JWT_SECRET = process.env.JWT_SECRET || 'your-secret-key'; // Use environment variable in production
const TOKEN_EXPIRY = '24h';

/**
 * Service for generating and validating JWT tokens
 */
exports.generateToken = (user) => {
  return jwt.sign(
    {
      userId: user._id,
      username: user.username,
      isAdmin: user.isAdmin
    },
    JWT_SECRET,
    { expiresIn: TOKEN_EXPIRY }
  );
};

exports.verifyToken = (token) => {
  try {
    return jwt.verify(token, JWT_SECRET);
  } catch (error) {
    return null;
  }
};