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

                const priorityCategories = categoriesData.slice(0, MAX_CATEGORIES);

                const categoriesWithMovies = await Promise.all(
                    priorityCategories.map(async (category) => {
                        const response = await fetch(
                            `http://localhost:4000/api/categories/${category._id}`,
                            { headers: headers }
                        );
                        const categoryData = await response.json();

                        const limitedMovies = categoryData.movies.slice(0, 20);

                        return {
                            id: categoryData._id,
                            name: categoryData.name,
                            movies: limitedMovies.map((movieTitle, index) => ({
                                id: index,
                                title: movieTitle,
                                imageUrl: "/api/placeholder/256/144"
                            }))
                        };
                    })
                );

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