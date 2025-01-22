import React from 'react';
import Button from '../../common/Button/Button';
import './CategoryList.css';

const CategoryList = ({ categories, onSelectCategory, onDeleteCategory, colors }) => {
    return (
        <div className="category-list" style={{ backgroundColor: colors.background.secondary }}>
            <h2 style={{ color: colors.text.primary }}>Categories</h2>
            <div className="categories-grid">
                {categories.map(category => (
                    <div 
                        key={category._id}
                        className="category-card"
                        style={{ backgroundColor: colors.background.tertiary }}
                    >
                        <div className="category-info">
                            <h3 style={{ color: colors.text.primary }}>{category.name}</h3>
                            <p style={{ color: colors.text.secondary }}>Movies: {category.movieCount}</p>
                            <p style={{ color: colors.text.secondary }}>
                                {category.promoted ? 'Promoted' : 'Not Promoted'} | 
                                Sort Order: {category.sortOrder}
                            </p>
                        </div>
                        <div className="category-actions">
                            <Button
                                onClick={() => onSelectCategory(category)}
                                className="secondary"
                            >
                                Edit
                            </Button>
                            <Button
                                onClick={() => onDeleteCategory(category._id)}
                                className="danger"
                            >
                                Delete
                            </Button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default CategoryList;
