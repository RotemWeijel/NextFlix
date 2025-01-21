import React, { useState, useEffect } from 'react';
import LoadingSpinner from '../../components/common/LoadingSpinner/LoadingSpinner';
import Button from '../../components/common/Button/Button';
import Input from '../../components/common/Input/Input';
import Navbar from '../../components/common/Navbar/Navbar';
import Footer from '../../components/common/Footer/Footer';
import { useTheme } from '../../hooks/useTheme';
import CategoryList from '../../components/admin/categories/CategoryList';
import CategoryForm from '../../components/admin/categories/CategoryForm';
import './CategoryManagement.css';

const CategoriesManagement = ({ tokenUser }) => {
    const { colors } = useTheme();
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isFormVisible, setIsFormVisible] = useState(false);
    const [selectedCategory, setSelectedCategory] = useState(null);
    const [feedback, setFeedback] = useState({ type: '', message: '' });

    useEffect(() => {
        if (tokenUser) {
            fetchCategories();
        }
    }, [tokenUser]);

    const fetchCategories = async () => {
        try {
            const headers = {
                'Authorization': `Bearer ${tokenUser}`,
                'Content-Type': 'application/json'
            };

            const response = await fetch('http://localhost:4000/api/categories', {
                headers: headers
            });
            const data = await response.json();
            setCategories(data);
            setLoading(false);
        } catch (error) {
            console.error('Error fetching categories:', error);
            setFeedback({
                type: 'error',
                message: 'Failed to fetch categories. Please try again.'
            });
            setLoading(false);
        }
    };

    const handleCreate = async (categoryData) => {
        try {
            const response = await fetch('http://localhost:4000/api/categories', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${tokenUser}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(categoryData)
            });

            if (response.ok) {
                setFeedback({
                    type: 'success',
                    message: 'Category created successfully!'
                });
                await fetchCategories();
                setIsFormVisible(false);
            } else {
                throw new Error('Failed to create category');
            }
        } catch (error) {
            setFeedback({
                type: 'error',
                message: 'Failed to create category. Please try again.'
            });
        }
    };

    const handleUpdate = async (categoryId, updatedData) => {
        try {
            const response = await fetch(`http://localhost:4000/api/categories/${categoryId}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${tokenUser}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updatedData)
            });

            if (response.ok) {
                setFeedback({
                    type: 'success',
                    message: 'Category updated successfully!'
                });
                await fetchCategories();
                setIsFormVisible(false);
                setSelectedCategory(null);
            } else {
                throw new Error('Failed to update category');
            }
        } catch (error) {
            setFeedback({
                type: 'error',
                message: 'Failed to update category. Please try again.'
            });
        }
    };

    const handleDelete = async (categoryId) => {
        if (window.confirm('Are you sure you want to delete this category?')) {
            try {
                const response = await fetch(`http://localhost:4000/api/categories/${categoryId}`, {
                    method: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${tokenUser}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (response.ok) {
                    setFeedback({
                        type: 'success',
                        message: 'Category deleted successfully!'
                    });
                    await fetchCategories();
                } else {
                    throw new Error('Failed to delete category');
                }
            } catch (error) {
                setFeedback({
                    type: 'error',
                    message: 'Failed to delete category. Please try again.'
            });
            }
        }
    };

    return (
        <div style={{ backgroundColor: colors.background.primary, color: colors.text.primary }}>
            <Navbar />
            <div className="categories-container">
                <h1 style={{ color: colors.text.primary }}>Categories Management</h1>
                
                {feedback.message && (
                    <div className={`feedback-message ${feedback.type}`}>
                        {feedback.message}
                    </div>
                )}

                <Button
                    onClick={() => setIsFormVisible(true)}
                    className="primary"
                >
                    Add New Category
                </Button>

                    <div className="categories-content">
                        <CategoryList 
                            categories={categories}
                            onSelectCategory={(category) => {
                                setSelectedCategory(category);
                                setIsFormVisible(true);
                            }}
                            onDeleteCategory={handleDelete}
                            colors={colors}
                        />
                        
                        {isFormVisible && (
                            <CategoryForm
                                category={selectedCategory}
                                onSubmit={selectedCategory ? handleUpdate : handleCreate}
                                onCancel={() => {
                                    setIsFormVisible(false);
                                    setSelectedCategory(null);
                                }}
                                categories={categories}
                                colors={colors}
                            />
                        )}
                    </div>
            </div>
            <Footer />
        </div>
    );
};

export default CategoriesManagement;