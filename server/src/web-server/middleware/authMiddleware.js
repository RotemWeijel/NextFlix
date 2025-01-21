// middleware/authMiddleware.js
const Token = require('../models/token'); 
const { verifyToken } = require('../services/token');

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
    const decoded = await verifyToken(token); // Use verifyToken service
    if (!decoded) {
      return res.status(401).json({ error: 'Unauthorized: Invalid or expired token' });
    }

    req.user = decoded; // Store full decoded user info
    req.userId = decoded.userId;
    next();
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};
