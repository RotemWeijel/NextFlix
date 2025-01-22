import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Button from '../../components/common/Button/Button';
import Input from '../../components/common/Input/Input';
import Navbar from '../../components/common/Navbar/Navbar';
import Footer from '../../components/common/Footer/Footer';
import { useTheme } from '../../hooks/useTheme';
import CategoryList from '../../components/admin/categories/CategoryList';
import CategoryForm from '../../components/admin/categories/CategoryForm';
import { getStoredToken, createAuthHeaders } from '../../utils/auth';
import './CategoryManagement.css';

const CategoriesManagement = () => {
    const navigate = useNavigate();
    const { colors } = useTheme();
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isFormVisible, setIsFormVisible] = useState(false);
    const [selectedCategory, setSelectedCategory] = useState(null);
    const [feedback, setFeedback] = useState({ type: '', message: '' });
    const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:4000';

    useEffect(() => {
        checkAuthAndFetchCategories();
    }, []);

    const checkAuthAndFetchCategories = async () => {
        if (!getStoredToken()) {
            navigate('/login');
            return;
        }
        await fetchCategories();
    };

    const fetchCategories = async () => {
        try {
            const headers = {
                ...createAuthHeaders(),
                'Content-Type': 'application/json'
            };
            const response = await fetch(`${API_BASE_URL}/api/categories`, {
                headers
            });
            
            if (!response.ok) {
                if (response.status === 401) {
                    navigate('/login');
                    return;
                }
                throw new Error('Failed to fetch categories');
            }
            
            const data = await response.json();
            setCategories(data);
        } catch (error) {
            console.error('Error fetching categories:', error);
            setFeedback({
                type: 'error',
                message: 'Failed to fetch categories. Please try again.'
            });
        } finally {
            setLoading(false);
        }
    };

    const handleCreate = async (categoryData) => {
        try {
            const headers = {
                ...createAuthHeaders(),
                'Content-Type': 'application/json'
            };

            const response = await fetch(`${API_BASE_URL}/api/categories`, {
                method: 'POST',
                headers,
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

    const handleUpdate = async (data) => {
        const { categoryId, updates } = data;
        if (!categoryId) {
            setFeedback({
                type: 'error',
                message: 'No category selected for update.'
            });
            return;
        }
    
        try {
            const headers = {
                ...createAuthHeaders(),
                'Content-Type': 'application/json'
            };

            const response = await fetch(`${API_BASE_URL}/api/categories/${categoryId}`, {
                method: 'PATCH',
                headers,
                body: JSON.stringify(updates)
            });
    
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.error || 'Failed to update category');
            }
    
            setFeedback({
                type: 'success',
                message: 'Category updated successfully!'
            });
            await fetchCategories();
            setIsFormVisible(false);
            setSelectedCategory(null);
        } catch (error) {
            setFeedback({
                type: 'error',
                message: error.message || 'Failed to update category. Please try again.'
            });
        }
    };

    const handleDelete = async (categoryId) => {
        if (window.confirm('Are you sure you want to delete this category?')) {
            try {
                const headers = {
                    ...createAuthHeaders(),
                    'Content-Type': 'application/json'
                };
                
                const response = await fetch(`${API_BASE_URL}/api/categories/${categoryId}`, {
                    method: 'DELETE',
                    headers
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
            <div className="image-container">
                <h1 style={{ color: colors.text.primary }}>Management Screen</h1>
                <img
                    src="/images/admin/admin.jpeg"
                    alt="Baby Image"
                    className = 'baby-image'
                />



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
                </Button >
                </div>

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
                </div>
            </div>
            <Footer />
        </div>
    );
};

export default CategoriesManagement;