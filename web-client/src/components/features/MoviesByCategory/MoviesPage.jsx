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
                const moviesResponse = await fetch('http://localhost:4000/api/movies', {
                    headers: headers
                });
                const allMovies = await moviesResponse.json();
                const moviesMap = allMovies.reduce((acc, movie) => {
                    acc[movie._id] = {
                        _id: movie._id,
                        name: movie.name,
                        imageUrl: movie.imageUrl,
                        categories: movie.categories
                    };
                    return acc;
                }, {});
                console.log(moviesMap)
                const categoriesResponse = await fetch('http://localhost:4000/api/categories', {
                    headers: headers
                });
                const categoriesData = await categoriesResponse.json();
                console.log('Categories data:', categoriesData);

                const priorityCategories = categoriesData.slice(0, MAX_CATEGORIES);

                const categoriesWithMovies = priorityCategories.map(category => {
                    const moviesList = Array.isArray(category.movies)
                        ? category.movies
                            .map(movieId => moviesMap[movieId])
                            .filter(movie => movie)
                            .map(movie => ({
                                _id: movie._id,
                                name: movie.name,
                                imageUrl: movie.imageUrl
                            }))
                        : [];

                    return {
                        id: category._id,
                        name: category.name,
                        movies: moviesList
                    };
                });

                console.log('Final categories with movies:', categoriesWithMovies);

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