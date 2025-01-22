const jwt = require('jsonwebtoken');
const Token = require('../models/token');

const JWT_SECRET = process.env.JWT_SECRET || 'your-secret-key';
const TOKEN_EXPIRY = '4h';

/**
 * Service for generating and validating JWT tokens
 */
exports.generateToken = async (user) => {
  const token = jwt.sign(
    {
      userId: user._id,
      username: user.username,
      isAdmin: user.isAdmin,
      full_name: user.full_name
    },
    JWT_SECRET,
    { expiresIn: TOKEN_EXPIRY }
  );

  // Save token to MongoDB
  await Token.create({
    userId: user._id,
    token: token
  });

  return token;
};

exports.verifyToken = async (token) => {
  try {
    const decoded = jwt.verify(token, JWT_SECRET);
    
    const tokenExists = await Token.findOne({ token });
    if (!tokenExists) {
      return null;
    }
    
    return decoded;
  } catch (error) {
    return null;
  }
};