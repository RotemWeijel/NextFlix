import React, { useState, useEffect } from 'react';
import MovieList from '../../common/MovieList/MovieList';
import { useTheme } from '../../../hooks/useTheme';
import './MoviesPage.css';

const MoviesPage = () => {
    const { colors } = useTheme();
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(true);


    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const token = '97f3f30c7512f8f507057e9c5752256a';
                const headers = {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                };

                const categoriesResponse = await fetch('http://localhost:4000/api/categories', {
                    headers: headers
                });
                const categoriesData = await categoriesResponse.json();

                const categoriesWithMovies = await Promise.all(
                    categoriesData.map(async (category) => {
                        const response = await fetch(
                            `http://localhost:4000/api/categories/${category._id}`,
                            { headers: headers }
                        );
                        const categoryData = await response.json();

                        return {
                            id: categoryData._id,
                            name: categoryData.name,
                            movies: categoryData.movies.map((movieTitle, index) => ({
                                id: index,
                                title: movieTitle,
                                imageUrl: "/api/placeholder/256/144"
                            }))
                        };
                    })
                );

                setCategories(categoriesWithMovies);
                setLoading(false);
            } catch (error) {
                console.error('Error fetching data:', error);
                setLoading(false);
            }
        };


        fetchCategories();
    }, []);

    return (
        <div className="movies-page" style={{
            '--background-primary': colors.background.primary,
            '--text-primary': colors.text.primary
        }}>

            {!loading && categories.map((category) => (
                <MovieList
                    title={category.name}
                    movies={category.movies}
                />
            ))}
        </div>
    );
};

export default MoviesPage;