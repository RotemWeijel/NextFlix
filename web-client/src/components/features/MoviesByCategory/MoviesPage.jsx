import React, { useState, useEffect } from 'react';
import MovieList from '../../common/MovieList/MovieList';
import { useTheme } from '../../../hooks/useTheme';
import './MoviesPage.css';

const MoviesPage = ({ tokenUser }) => {
    const { colors } = useTheme();
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(true);
    const MAX_CATEGORIES = 5;

    useEffect(() => {
        const fetchCategories = async () => {
            try {

                const headers = {
                    'Authorization': `Bearer ${tokenUser}`,
                    'Content-Type': 'application/json'
                };
                const categoriesResponse = await fetch('http://localhost:4000/api/categories', {
                    headers: headers
                });
                const categoriesData = await categoriesResponse.json();
                const categoriesWithMovies = await Promise.all(categoriesData.map(async category => {
                    const movieDetails = await Promise.all(category.movies.map(async movieId => {
                        try {
                            const movieResponse = await fetch(`http://localhost:4000/api/movies/${movieId}`, {
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
        if (tokenUser) {
            fetchCategories();
        }
    }, [tokenUser]);

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