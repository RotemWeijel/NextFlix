const express = require('express');
const tokenController = require('../controllers/tokens');
const authMiddleware = require('../middleware/authMiddleware');

const router = express.Router();

router.post('/', tokenController.login); // Public route

module.exports = router;