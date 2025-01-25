import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTheme } from '../../hooks/useTheme';
import Input from '../../components/common/Input/Input';
import Button from '../../components/common/Button/Button';
import '../../components/admin/movies/MovieForm.css';
import Footer from '../../components/common/Footer/Footer';
import Navbar from '../../components/common/Navbar/Navbar';
import { getStoredToken, createAuthHeaders } from '../../utils/auth' 
import { useParams } from 'react-router-dom';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:4000';

const defaultColors = {
    background: {
        primary: '#000000',
        secondary: '#1a1a1a'
    },
    text: {
        primary: '#ffffff',
        secondary: '#cccccc'
    }
};

const MovieManagement = () => {

    const navigate = useNavigate();
    const { id: movieId } = useParams();
    const { colors } = useTheme();

    const safeColors = {
        background: {
            ...defaultColors.background,
            ...(colors?.background || {})
        },
        text: {
            ...defaultColors.text,
            ...(colors?.text || {})
        }
    };

    const [formData, setFormData] = useState({
        name: '',
        description: '',
        duration: '',
        releaseYear: '',
        actors: [{ name: '' }],
        categories: [],
        ageAllow: '',
        director: '',
        language: '',
        imageUrl: '',
        trailerUrl: '',
        videoUrl: ''
    });

    const [feedback, setFeedback] = useState({ type: '', message: '' });
    const [isLoading, setIsLoading] = useState(true);
    const [categories, setCategories] = useState([]);

useEffect(() => {
    const fetchCategories = async () => {
        try {
            const response = await fetch(`${API_BASE_URL}/api/categories`, {
                headers: {
                    ...createAuthHeaders(),
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                const data = await response.json();
                setCategories(data);
            } else {
                throw new Error('Failed to fetch categories');
            }
        } catch (error) {
            console.error('Error fetching categories:', error);
            setFeedback({
                type: 'error',
                message: 'Failed to load categories. Please try again.'
            });
        }
    };

    fetchCategories();
}, []);


    useEffect(() => {
        if (!getStoredToken()) {
            navigate('/login');
            return;
        }

        const fetchMovie = async () => {
            try {
                const response = await fetch(`${API_BASE_URL}/api/movies/${movieId}`, {
                    headers: {
                        ...createAuthHeaders(),
                        'Content-Type': 'application/json'
                    }
        
                });

                if (response.ok) {
                    const movieData = await response.json();
                    setFormData(movieData);
                } else {
                    throw new Error('Failed to fetch movie data');
                }
            } 
            catch (error) {
                console.error('Error details:', error);
                setFeedback({
                    type: 'error',
                    message: 'Failed to update movie. Please try again.'
                });
            } finally {
                setIsLoading(false);
            }
        };

        if (movieId) {
            fetchMovie();
        } else {
            setIsLoading(false);
            setFeedback({
                type: 'error',
                message: 'No movie ID provided'
            });
        }
    }, [movieId]);

    if (!getStoredToken()) {
        navigate('/login');
        return;
    }

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
        try {
            // Create a copy of the form data
            const processedData = { ...formData };
    
            // Clean up empty values
            if (processedData.categories.length === 0) {
                processedData.categories = null;
            }
            
            // Clean up empty strings in actors array
            processedData.actors = processedData.actors.filter(actor => actor.name.trim() !== '');
            if (processedData.actors.length === 0) {
                processedData.actors = null;
            }
    
            // Convert empty strings to null
            Object.keys(processedData).forEach(key => {
                if (processedData[key] === '') {
                    processedData[key] = null;
                }
            });
    
            const response = await fetch(`${API_BASE_URL}/api/movies/${movieId}`, {
                method: 'PUT',
                headers: {
                    ...createAuthHeaders(),
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(processedData)
            });
    
            if (response.ok) {
                setFeedback({
                    type: 'success',
                    message: 'Movie updated successfully!'
                });
                setTimeout(() => navigate('/movies/browse'), 2000);
            } else {
                throw new Error('Failed to update movie');
            }
        } catch (error) {
            setFeedback({
                type: 'error',
                message: 'Failed to update movie. Please try again.'
            });
        }
    };

    const handleDelete = async () => {
        if (window.confirm('Are you sure you want to delete this movie?')) {
            try {
                const response = await fetch(`${API_BASE_URL}/api/movies/${movieId}`, {
                    method: 'DELETE',
                    headers: {
                        ...createAuthHeaders(),
                        'Content-Type': 'application/json'
                    }
                });

                if (response.ok) {
                    setFeedback({
                        type: 'success',
                        message: 'Movie deleted successfully!'
                    });
                    setTimeout(() => navigate('/movies/browse'), 2000);
                } else {
                    throw new Error('Failed to delete movie');
                }
            } catch (error) {
                setFeedback({
                    type: 'error',
                    message: 'Failed to delete movie. Please try again.'
                });
            }
        }
    };

    if (isLoading) {
        return <div style={{ color: safeColors.text.primary }}>Loading...</div>;
    }

    return (
        <div>
            <div className="movie-form" style={{ backgroundColor: safeColors.background.secondary }}>
                <Navbar />
                <h2 style={{ color: safeColors.text.primary }}>Edit Movie</h2>
                <img
                    src="/images/admin/admin.jpeg"
                    alt="Baby Image"
                    className='baby-image'
                />
                {feedback.message && (
                    <div className={`feedback-message ${feedback.type}`} style={{ color: safeColors.text.primary }}>
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
    <h3 style={{ color: safeColors.text.primary }}>Categories</h3>
    <div className="categories-grid">
        {categories.map((cat) => (
            <div key={cat._id} className="category-checkbox">
                <label style={{ color: safeColors.text.primary }}>
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
                        <h3 style={{ color: safeColors.text.primary }}>Actors</h3>
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
                        <Input
                            label="Image URL"
                            value={formData.imageUrl}
                            onChange={(e) => setFormData({ ...formData, imageUrl: e.target.value })}
                        />
                    </div>

                    <div className="form-group">
                        <Input
                            label="Trailer URL"
                            value={formData.trailerUrl}
                            onChange={(e) => setFormData({ ...formData, trailerUrl: e.target.value })}
                        />
                    </div>

                    <div className="form-group">
                        <Input
                            label="Video URL"
                            value={formData.videoUrl}
                            onChange={(e) => setFormData({ ...formData, videoUrl: e.target.value })}
                            required
                        />
                    </div>

                    <div className="form-actions" style={{ display: 'flex', gap: '10px', justifyContent: 'space-between' }}>
                        <div>
                            <Button
                                type="submit"
                                className="primary"
                                style={{ marginLeft: '10px' }}
                            >
                                Save Changes
                            </Button>
                            <Button
                                type="button"
                                onClick={() => navigate('/movies/browse')}
                                className="secondary"
                            >
                                Cancel
                            </Button>

                        </div>
                        <Button
                            type="button"
                            onClick={handleDelete}
                            className="danger"
                        >
                            Delete Movie
                        </Button>
                    </div>
                </form>
            </div>
            <Footer />
        </div>

    );
};

export default MovieManagement;