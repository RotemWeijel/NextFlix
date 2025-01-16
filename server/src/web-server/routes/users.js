// User Routes - defines endpoints for user-related operations
const express = require('express');
const userController = require('../controllers/users');
const authMiddleware = require('../middleware/authMiddleware');

const router = express.Router();

router.post('/', userController.register); // Public route
router.get('/:id', authMiddleware, userController.getUser); // Protected route

module.exports = router;
