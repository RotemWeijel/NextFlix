import React, { useState, useEffect } from 'react';
import './DetailsMovie.css';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:4000';


const DetailsMovie = ({ movieId, tokenUser }) => {
    const [movie, setMovie] = useState(null)
    const [showAllCast, setShowAllCast] = useState(false);
    useEffect(() => {
        const fetchMovie = async () => {
            try {
                const actualId = typeof movieId === 'object' ? movieId.movie : movieId;
                const headers = {
                    'Authorization': `Bearer ${tokenUser}`,
                    'Content-Type': 'application/json'
                };
                const url = `${API_BASE_URL}/api/movies/${actualId}`;

                const res = await fetch(url, {
                    headers: headers
                });

                if (!res.ok) {
                    throw new Error(`HTTP error! status: ${res.status}`);
                }
                const data = await res.json();
                setMovie(data);
            } catch (error) {
                console.error('Error fetching movie:', error);
            }
        };

        if (movieId) {
            fetchMovie();
        }
    }, [movieId]);
    if (!movie) {
        return null;
    }


    const getAgeRating = (ageAllow) => `${ageAllow}+`;

    const movieTraits = [
        "Emotional",
        `${movie.language} language`,
        `Directed by ${movie.director}`,
        `${movie.duration} minutes`
    ];

    return (
        <div className="movie-details-container">
            <div className="movie-details-wrapper">
                {/* Left Section */}
                <div className="left-section">
                    <div className="year-info">
                        <span>{movie.releaseYear}</span>
                        <span className="quality">HD</span>
                        <span className="age-rating">{getAgeRating(movie.ageAllow)}</span>
                    </div>
                    <p className="description">
                        {movie.description}
                    </p>
                </div>

                {/* Right Section */}
                <div className="right-section">
                    <div className="metadata-grid">
                        <div className="metadata-row">
                            <span className="metadata-label">Cast:</span>
                            <span className="metadata-value">
                                {movie.actors?.slice(0, showAllCast ? movie.actors.length : 3).map((actor, index) => (
                                    <React.Fragment key={`actor-${index}`}>
                                        {index > 0 && ", "}
                                        {actor.name}
                                    </React.Fragment>
                                ))}
                                {!showAllCast && movie.actors?.length > 3 && (
                                    <span className="more" onClick={() => setShowAllCast(true)}>, more</span>
                                )}
                            </span>
                        </div>

                        <div className="metadata-row">
                            <span className="metadata-label">Genres:</span>
                            <span className="metadata-value">
                                {movie.categories?.join(", ")}
                            </span>
                        </div>

                        <div className="metadata-row">
                            <span className="metadata-label">This movie is:</span>
                            <span className="metadata-value">
                                {movieTraits.join(", ")}
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default DetailsMovie;