const categorycontrollers = require('../controllers/categories')
const verifyAuth = require('../middleware/authMiddleware')
const requireAdmin = require('../middleware/adminMiddlewere')
const express = require('express')
var router = express.Router();

router.route('/')
    .get(verifyAuth, categorycontrollers.getCategories)
    .post(verifyAuth, requireAdmin, categorycontrollers.createCategory)

router.route('/:id')
    .get(verifyAuth, categorycontrollers.getCategoryById)
    .patch(verifyAuth, requireAdmin, categorycontrollers.patchCategory)
    .delete(verifyAuth, requireAdmin, categorycontrollers.deleteCategory)

module.exports = router;