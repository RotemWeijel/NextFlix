const Category = require('../models/categories')
const Movie = require('../models/movies')
const createCategory = async (name, promoted, description, sortOrder, movieCount, displayInMenu) => {
    try {
        const category = new Category({
            name: name, promoted: promoted,
            description: description, sortOrder: sortOrder, movieCount: movieCount, displayInMenu: displayInMenu
        })
        const savedCategory = await category.save()
        return { success: true, content: savedCategory }
    } catch (error) {
        if (error.code === 11000) {
            return { success: false, error: 'Category name already exists' }
        }
        return { success: false, error: 'Error creating category', details: error.message }
    }
}

const getCategories = async () => {
    try {
        const categories = await Category.find({})
        return { success: true, data: categories }
    } catch (error) {
        return { success: false, error: 'Error fetching categories', details: error.message }
    }
}
const getCategoryById = async (id) => {
    try {
        const category = await Category.findById(id)
        if (!category) {
            return { success: false, error: 'Category not found' }
        }
        return { success: true, data: category }
    } catch (error) {
        return { success: false, error: 'Error fetching category' }
    }
}
const patchCategory = async (id, updates) => {
    try {
        //check the category exist first and the new name not exist
        const category = await getCategoryById(id)
        if (!category) {
            return { success: false, error: 'Category not found' }
        }
        if (updates.name) {
            const existingCategory = await Category.findOne({
                name: updates.name,
                _id: { $ne: id }
            })
            if (existingCategory) {
                return { success: false, error: 'Category name already exists' }
            }
        }
        // update the category
        const updatecategory = await Category.findByIdAndUpdate(
            id,
            { $set: updates },
            { new: true, runValidators: true }
        )
        return { success: true, data: updatecategory }
    }
    catch (error) {
        return { success: false, error: 'Error updating category' }
    }
}
const deleteCategory = async (id) => {
    try {
        //look for this category if there is no return not found
        const category = await Category.findById(id)
        if (!category) {
            return { success: false, error: 'Category not found' }
        }
        //first update all movies under this category to remove this category from their list
        await Movie.updateMany(
            { categories: id },
            { $pull: { categories: id } }
        );
        //next remove all movie that her list of category empty
        await Movie.deleteMany(
            { categories: { $size: 0 } }
        );
        await Category.findByIdAndDelete(id)
        return { success: true }
    }
    catch (error) {
        return { success: false, error: 'Error deleting category' }
    }
}

module.exports = { createCategory, getCategories, getCategoryById, patchCategory, deleteCategory }