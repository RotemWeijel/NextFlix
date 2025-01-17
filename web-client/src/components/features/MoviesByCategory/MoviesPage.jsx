import React, { useState, useEffect } from 'react';
import MovieList from '../../common/MovieList/MovieList';
import './MoviesPage.css';

const MoviesPage = () => {
    
    const trendingMovies = [
        {
            id: 1,
            title: "Money Heist",
            imageUrl: "/api/placeholder/256/144"
        },
        {
            id: 2,
            title: "Squid Game",
            imageUrl: "/api/placeholder/256/144"
        },
        {
            id: 3,
            title: "Stranger Things",
            imageUrl: "/api/placeholder/256/144"
        },
        {
            id: 4,
            title: "Breaking Bad",
            imageUrl: "/api/placeholder/256/144"
        },
        {
            id: 5,
            title: "The Crown",
            imageUrl: "/api/placeholder/256/144"
        }
    ];

    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const categoriesResponse = await fetch('http://localhost:3000/api/categories');
                const categoriesData = await categoriesResponse.json();

                const categoriesWithMovies = await Promise.all(
                    categoriesData.map(async (category) => {
                        const response = await fetch(`http://localhost:3000/api/movies/category/${category.id}`);
                        const movies = await response.json();
                        return {
                            ...category,
                            movies: movies.map(movie => ({
                                id: movie.id,
                                title: movie.title,
                                imageUrl: movie.imageUrl || "/api/placeholder/256/144"
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
        <div className="movies-page">
            <MovieList title="Trending Now" movies={trendingMovies} />

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