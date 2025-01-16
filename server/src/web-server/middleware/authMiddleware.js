// middleware/authMiddleware.js
const Token = require('../models/token'); 

module.exports = async (req, res, next) => {
  const authHeader = req.headers['authorization'];
  if (!authHeader) {
    return res.status(401).json({ error: 'Unauthorized: Authorization header is missing' });
  }

  const token = authHeader.split(' ')[1]; // Extract token
  if (!token) {
    return res.status(401).json({ error: 'Unauthorized: Token is missing' });
  }

  try {
    const validToken = await Token.findOne({ token }); // Check if the token exists in the DB- which means that
    //some user have logged in and got this token
    if (!validToken) {
      return res.status(401).json({ error: 'Unauthorized: Invalid or expired token' });
    }

    req.userId = validToken.userId;
    next();
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};
