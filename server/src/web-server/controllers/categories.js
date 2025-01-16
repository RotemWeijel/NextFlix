const categoryServices = require('../services/categories')

const createCategory = async (req, res) => {
    if (!req.body.name) {
        return res.status(400).json({ error: "Name is required" });
    }
    const result = await categoryServices.createCategory(
        req.body.name, req.body.promoted, req.body.description, req.body.sortOrder,
        req.body.movieCount, req.body.displayInMenu)
    if (!result.success) {
        return res.status(400).json({ error: result.error });
    }
    return res.status(201)
        .location(`/api/categories/${result.content._id}`)
        .send();
};

const getCategories = async (req, res) => {
    const result = await categoryServices.getCategories()
    if (!result.success) {
        return res.status(400).json({ error: result.error });
    }
    return res.status(200).json(result.data)
};

const getCategoryById = async (req, res) => {
    const result = await categoryServices.getCategoryById(req.params.id)
    if (!result.success) {
        return res.status(404).json({ error: "Category not found" });
    }
    return res.status(200).json(result.data)
};

const patchCategory = async (req, res) => {
    const result = await categoryServices.patchCategory(req.params.id, req.body)
    if (!result.success) {
        if (result.error === "Category name already exists") {
            return res.status(400).json({ error: result.error });
        }
        return res.status(404).json({ error: "Category not found" });
    }
    return res.status(204).send();
};

const deleteCategory = async (req, res) => {
    const idCategory = req.params.id
    const result = await categoryServices.deleteCategory(idCategory)
    if (!result.success) {
        return res.status(404).json({ error: "Category not found" });
    }
    return res.status(204).send();
};

module.exports = { createCategory, getCategories, getCategoryById, patchCategory, deleteCategory }
