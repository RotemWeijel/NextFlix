import React, { useState } from 'react';
import Input from '../../common/Input/Input';
import Button from '../../common/Button/Button';
import { useParams, useNavigate } from 'react-router-dom';
import { getStoredToken, createAuthHeaders } from '../../../utils/auth';
import './MovieForm.css';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:4000';

const MovieForm = ({ onSubmit, onCancel, categoryId, categories, colors }) => {
    const [formData, setFormData] = useState({
        name: '',
        description: '',
        duration: '',
        releaseYear: new Date().getFullYear(),
        actors: [{ name: '' }],
        categories: [categoryId], // Initialize with current category
        ageAllow: '',
        director: '',
        language: '',
        imageUrl: '',
        trailerUrl: '',
        videoUrl: ''
    });
    const [uploading, setUploading] = useState(false);

    const uploadFile = async (file, type) => {
        console.log('Starting upload for:', type, file);
        const formData = new FormData();
        formData.append(type, file);
    
        try {
            console.log('Making request to:', `${API_BASE_URL}/api/upload/${type}`);
            const response = await fetch(`${API_BASE_URL}/api/upload/${type}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${getStoredToken()}`
                },
                body: formData
            });
            console.log('Upload response:', response);
    
            if (response.ok) {
                const data = await response.json();
                console.log('Upload success:', data);
                return data.url;
            }
        } catch (error) {
            console.error('Upload error details:', error);
            throw error;
        }
    };

    const handleFileChange = async (e, type) => {
        const file = e.target.files[0];
        if (!file) return;

        setUploading(true);
        try {
            const url = await uploadFile(file, type);
            setFormData(prev => ({
                ...prev,
                [type === 'image' ? 'imageUrl' : type === 'video' ? 'videoUrl' : 'trailerUrl']: url
            }));
            setFeedback({
                type: 'success',
                message: `${type} uploaded successfully`
            });
        } catch (error) {
            console.error('File upload error:', error);
        } finally {
            setUploading(false);
        }
    };

    const navigate = useNavigate(); 
    const [feedback, setFeedback] = useState({ type: '', message: '' });

    if (!getStoredToken()) {
        navigate('/login');
    }

    // Add handler for category changes
    const handleCategoryChange = (categoryId) => {
        const updatedCategories = formData.categories.includes(categoryId)
            ? formData.categories.filter(id => id !== categoryId)
            : [...formData.categories, categoryId];
        
        setFormData({ ...formData, categories: updatedCategories });
    };

    const handleActorChange = (index, value) => {
        const newActors = [...formData.actors];
        newActors[index] = { name: value };
        setFormData({ ...formData, actors: newActors });
    };

    const addActor = () => {
        setFormData({
            ...formData,
            actors: [...formData.actors, { name: '' }]
        });
    };

    const removeActor = (index) => {
        const newActors = formData.actors.filter((_, i) => i !== index);
        setFormData({ ...formData, actors: newActors });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const processedData = {
            ...formData,
            duration: parseInt(formData.duration),
            releaseYear: parseInt(formData.releaseYear),
            ageAllow: parseInt(formData.ageAllow)
        };

        try {
            const response = await fetch(`${API_BASE_URL}/api/movies`, {
                method: 'POST',
                headers: {
                    ...createAuthHeaders(),
                    'Content-Type': 'application/json'
                },
    
                body: JSON.stringify(processedData)
            });

            if (response.ok) {
                const movie = await response.json();
                onSubmit(movie);
                setFeedback({
                    type: 'success',
                    message: 'Movie added successfully!'
                });
            } else {
                throw new Error('Failed to create movie');
            }
        } catch (error) {
            console.error('Error creating movie:', error);
            setFeedback({
                type: 'error',
                message: 'Failed to add movie. Please try again.'
            });
        }
    };

    return (
        <div className="movie-form" style={{ backgroundColor: colors.background.secondary }}>
            <h2 style={{ color: colors.text.primary }}>Add New Movie</h2>
            {feedback.message && (
                <div className={`feedback-message ${feedback.type}`}>
                    {feedback.message}
                </div>
            )}
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
                        required
                    />
                </div>

                <div className="form-group">
                    <Input
                        type="number"
                        label="Duration (minutes)"
                        value={formData.duration}
                        onChange={(e) => setFormData({ ...formData, duration: e.target.value })}
                        required
                        min="1"
                    />
                </div>

                <div className="form-group">
                    <Input
                        type="number"
                        label="Release Year"
                        value={formData.releaseYear}
                        onChange={(e) => setFormData({ ...formData, releaseYear: e.target.value })}
                        required
                    />
                </div>

                <div className="categories-section">
                    <h3 style={{ color: colors.text.primary }}>Categories</h3>
                    <div className="categories-grid">
                        {categories.map((cat) => (
                            <div key={cat._id} className="category-checkbox">
                                <label style={{ color: colors.text.primary }}>
                                    <Input
                                        type="checkbox"
                                        checked={formData.categories.includes(cat._id)}
                                        onChange={() => handleCategoryChange(cat._id)}
                                    />
                                    {cat.name}
                                </label>
                            </div>
                        ))}
                    </div>
                </div>

                <div className="actors-section">
                    <h3 style={{ color: colors.text.primary }}>Actors</h3>
                    {formData.actors.map((actor, index) => (
                        <div key={index} className="actor-input">
                            <div className="form-group">
                                <Input
                                    label={`Actor ${index + 1}`}
                                    value={actor.name}
                                    onChange={(e) => handleActorChange(index, e.target.value)}
                                    required
                                />
                            </div>
                            {index > 0 && (
                                <Button
                                    type="button"
                                    onClick={() => removeActor(index)}
                                    className="danger"
                                >
                                    Remove
                                </Button>
                            )}
                        </div>
                    ))}
                    <Button
                        type="button"
                        onClick={addActor}
                        className="secondary"
                    >
                        Add Actor
                    </Button>
                </div>

                <div className="form-group">
                    <Input
                        type="number"
                        label="Age Restriction"
                        value={formData.ageAllow}
                        onChange={(e) => setFormData({ ...formData, ageAllow: e.target.value })}
                        required
                    />
                </div>

                <div className="form-group">
                    <Input
                        label="Director"
                        value={formData.director}
                        onChange={(e) => setFormData({ ...formData, director: e.target.value })}
                        required
                    />
                </div>

                <div className="form-group">
                    <Input
                        label="Language"
                        value={formData.language}
                        onChange={(e) => setFormData({ ...formData, language: e.target.value })}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Movie Poster</label>
                    <Input 
                        type="file"
                        accept="image/*"
                        onChange={(e) => handleFileChange(e, 'image')}
                    />
                    {formData.imageUrl && <img src={formData.imageUrl} alt="Preview" className="preview" />}
                </div>

                <div className="form-group">
                    <label>Trailer Video</label>
                    <Input
                        type="file" 
                        accept="video/*"
                        onChange={(e) => handleFileChange(e, 'trailer')}
                    />
                    {formData.trailerUrl && <p>Trailer uploaded</p>}
                </div>

                <div className="form-group">
                    <label>Movie File</label>
                    <Input
                        type="file"
                        accept="video/*"
                        onChange={(e) => handleFileChange(e, 'video')}
                        required
                    />
                    {formData.videoUrl && <p>Movie uploaded</p>}
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
                        Add Movie
                    </Button>
                </div>
            </form>
        </div>
    );
};

export default MovieForm;
