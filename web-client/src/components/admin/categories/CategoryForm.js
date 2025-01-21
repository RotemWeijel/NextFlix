import React, { useState, useEffect } from 'react';
import Input from '../../common/Input/Input';
import Button from '../../common/Button/Button';
import './CategoryForm.css';

const CategoryForm = ({ category, onSubmit, onCancel, categories, colors }) => {
    const [formData, setFormData] = useState({
        name: '',
        promoted: false,
        description: '',
        sortOrder: 0,
        parentCategory: '',
        displayInMenu: true
    });

    useEffect(() => {
        if (category) {
            setFormData({
                name: category.name || '',
                promoted: category.promoted || false,
                description: category.description || '',
                sortOrder: category.sortOrder || 0,
                parentCategory: category.parentCategory || '',
                displayInMenu: category.displayInMenu ?? true
            });
        }
    }, [category]);

    const handleSubmit = (e) => {
        e.preventDefault();
        if (category) {
            onSubmit(category._id, formData);
        } else {
            onSubmit(formData);
        }
    };

    return (
        <div className="category-form" style={{ backgroundColor: colors.background.secondary }}>
            <h2 style={{ color: colors.text.primary }}>
                {category ? 'Edit Category' : 'Create New Category'}
            </h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <Input
                        label="Name"
                        value={formData.name}
                        onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                        required
                    />
                </div>

                <div className="form-group">
                    <Input
                        label="Description"
                        value={formData.description}
                        onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                        multiline
                    />
                </div>

                <div className="form-group">
                    <Input
                        type="number"
                        label="Sort Order"
                        value={formData.sortOrder}
                        onChange={(e) => setFormData({ ...formData, sortOrder: parseInt(e.target.value) })}
                    />
                </div>

                <div className="form-group">
                    <label style={{ color: colors.text.primary }}>
                        <Input
                            type="checkbox"
                            checked={formData.promoted}
                            onChange={(e) => setFormData({ ...formData, promoted: e.target.checked })}
                        />
                        Promoted
                    </label>
                </div>

                <div className="form-group">
                    <label style={{ color: colors.text.primary }}>
                        <Input
                            type="checkbox"
                            checked={formData.displayInMenu}
                            onChange={(e) => setFormData({ ...formData, displayInMenu: e.target.checked })}
                        />
                        Display in Menu
                    </label>
                </div>

                <div className="form-actions">
                    <Button
                        type="button"
                        onClick={onCancel}
                        className="secondary"
                    >
                        Cancel
                    </Button>
                    <Button
                        type="submit"
                        className="primary"
                    >
                        {category ? 'Save Changes' : 'Create Category'}
                    </Button>
                </div>
            </form>
        </div>
    );
};

export default CategoryForm;