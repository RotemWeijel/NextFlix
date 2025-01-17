import React from 'react'
import MovieCard from "../MovieCard/MovieCard"
import './MovieList.css'
const CategoryRow = ({ title, movies }) => {
    return (
        <div className="movie-list-container">
            <h2 className="category-title">{title}</h2>
            <div className="movies-row">
                <div className="movies-slider">
                    {movies.map((movie) => (
                        <div key={movie.id} className="movie-item">
                            <MovieCard src={movie.imageUrl} name={movie.title} />
                        </div>
                    ))}
                </div>
            </div>
        </div>
    )
}



export default CategoryRow