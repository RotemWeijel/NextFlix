import React, { useState } from 'react'
import MovieCard from "../MovieCard/MovieCard"
import './MovieList.css'
const MovieList = ({ title, movies }) => {
    const [scrollPosition, setScrollPosition] = useState(0);
    const handleScroll = (direction) => {
        const slider = document.querySelector(`#slider-${title.replace(/\s+/g, '-')}`);
        if (!slider) return;

        const scrollAmount = 250;
        const maxScroll = slider.scrollWidth - slider.clientWidth;

        let newPosition;
        if (direction === 'left') {
            newPosition = Math.max(scrollPosition - scrollAmount, 0);
        } else {
            newPosition = Math.min(scrollPosition + scrollAmount, maxScroll);
        }

        slider.scrollTo({
            left: newPosition,
            behavior: 'smooth'
        });

        setScrollPosition(newPosition);
    };
    return (
        <div className="movie-list-container">
            <h2 className="category-title">{title}</h2>
            <div className="movies-row">
                {scrollPosition > 0 && (
                    <button
                        className="slider-arrow slider-arrow-left"
                        onClick={() => handleScroll('left')}
                        aria-label="Scroll left"
                    >
                        ‹
                    </button>
                )}

                <div
                    id={`slider-${title.replace(/\s+/g, '-')}`}
                    className="movies-slider"
                >
                    {movies.map((movie) =>
                    (
                        <div key={movie._id} className="movie-item">
                            <MovieCard
                                src={movie.imageUrl}
                                name={movie.name}
                                movieId={movie._id}
                            />
                        </div>
                    ))}
                </div>

                <button
                    className="slider-arrow slider-arrow-right"
                    onClick={() => handleScroll('right')}
                    aria-label="Scroll right"
                >
                    ›
                </button>
            </div>
        </div>
    );
};




export default MovieList