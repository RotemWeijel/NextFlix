import React, { useState, useEffect } from 'react';
import MovieList from '../../common/MovieList/MovieList';
import { useTheme } from '../../../hooks/useTheme';
import './MoviesPage.css';
import { getStoredToken, createAuthHeaders } from '../../../utils/auth';
import { useNavigate } from 'react-router-dom';
const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:4000';

const MoviesPage = ({ }) => {
    const { colors } = useTheme();
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(true);
    const MAX_CATEGORIES = 5;
    const navigate = useNavigate()

    useEffect(() => {
        if (!getStoredToken()) {
            navigate('/login');
        }
    }, []);

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const headers = {
                    ...createAuthHeaders(),
                    'Content-Type': 'application/json'
                };
                const categoriesResponse = await fetch(`${API_BASE_URL}/api/categories`, {
                    headers: headers
                });
                const categoriesData = await categoriesResponse.json();
                const categoriesWithMovies = await Promise.all(categoriesData.map(async category => {
                    const movieDetails = await Promise.all(category.movies.map(async movieId => {
                        try {
                            const movieResponse = await fetch(`${API_BASE_URL}/api/movies/${movieId}`, {
                                headers: headers
                            });
                            const movieData = await movieResponse.json();
                            return {
                                _id: movieId,
                                name: movieData.name,
                                imageUrl: movieData.imageUrl
                            };
                        } catch (error) {
                            console.error(`Error fetching movie ${movieId}:`, error);
                            return null;
                        }
                    }));

                    const validMovies = movieDetails.filter(movie => movie !== null);

                    return {
                        id: category._id,
                        name: category.name,
                        movies: validMovies
                    };
                }));



                const organizedCategories = organizeCategories(categoriesWithMovies);
                setCategories(organizedCategories);
                setLoading(false);

            } catch (error) {
                console.error('Error fetching data:', error);
                setLoading(false);
            }
        };

        fetchCategories();

    }, []);

    const organizeCategories = (categories) => {
        return categories.sort((a, b) => {
            const priorityOrder = {
                'Popular': 1,
                'Trending': 2,
                'Continue Watching': 3,
                'New Releases': 4,
            };

            return (priorityOrder[a.name] || 999) - (priorityOrder[b.name] || 999);
        });
    };

    return (
        <div className="movies-page" style={{
            '--background-primary': colors.background.primary,
            '--text-primary': colors.text.primary
        }}>

            {!loading && categories.map((category) => (
                <MovieList
                    key={category.id}
                    title={category.name}
                    movies={category.movies}
                />
            ))}
        </div>
    );
};

export default MoviesPage;