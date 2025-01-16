const categorycontrollers = require('../controllers/categories')
const verifyAuth = require('../middleware/authMiddleware')
const express = require('express')
var router = express.Router();

router.route('/')
    .get(verifyAuth, categorycontrollers.getCategories)
    .post(verifyAuth, categorycontrollers.createCategory)

router.route('/:id')
    .get(verifyAuth, categorycontrollers.getCategoryById)
    .patch(verifyAuth, categorycontrollers.patchCategory)
    .delete(verifyAuth, categorycontrollers.deleteCategory)

module.exports = router;


