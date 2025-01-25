const express = require('express');
const router = express.Router();
const upload = require('../middleware/uploadMiddleware');
const verifyAuth = require('../middleware/authMiddleware');
const requireAdmin = require('../middleware/adminMiddlewere');

const API_BASE_URL = process.env.API_BASE_URL || 'http://localhost:4000';

router.post('/image', verifyAuth, requireAdmin, upload.single('image'), (req, res) => {
  res.json({ url: `${API_BASE_URL}/uploads/${req.file.filename}` });
});

router.post('/trailer', verifyAuth, requireAdmin, upload.single('trailer'), (req, res) => {
    res.json({ url: `${API_BASE_URL}/uploads/${req.file.filename}` });
});

router.post('/video', verifyAuth, requireAdmin, upload.single('video'), (req, res) => {
  res.json({ url: `${API_BASE_URL}/uploads/${req.file.filename}` });
});

module.exports = router;