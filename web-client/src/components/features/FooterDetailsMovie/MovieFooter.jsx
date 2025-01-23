import React from 'react';
import './MovieFooter.css';
import { useState, useEffect } from 'react';
import { getStoredToken, createAuthHeaders } from '../../../utils/auth';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:4000';


const MovieFooter = ({ movieId }) => {
    const [movie, setMovie] = useState(null)
     
    useEffect(() => {
        const fetchMovie = async () => {
            try {
                const actualId = typeof movieId === 'object' ? movieId.movie : movieId;
                const headers = {
                    ...createAuthHeaders(),
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
    return (
        <div className="footer-wrapper">
            <div className="movie-footer">
                <h2 className="footer-title">About {movie.name}</h2>
                <div className="footer-content">
                    <div className="footer-row">
                        <span className="label">Director:</span>
                        <span className="value">{movie.director}</span>
                    </div>

                    {movie.actors && movie.actors.length > 0 && (
                        <div className="footer-row">
                            <span className="label">Cast:</span>
                            <span className="value">
                                {movie.actors.map(actor => actor.name).join(', ')}
                            </span>
                        </div>
                    )}

                    {movie.categories && movie.categories.length > 0 && (
                        <div className="footer-row">
                            <span className="label">Genres:</span>
                            <span className="value">{movie.categories.join(', ')}</span>
                        </div>
                    )}

                    <div className="footer-row">
                        <span className="label">Language:</span>
                        <span className="value">{movie.language}</span>
                    </div>

                    <div className="footer-row">
                        <span className="label">Release Year:</span>
                        <span className="value">{movie.releaseYear}</span>
                    </div>

                    <div className="footer-row">
                        <span className="label">Duration:</span>
                        <span className="value">{movie.duration} minutes</span>
                    </div>

                    <div className="footer-row">
                        <span className="label">Description:</span>
                        <span className="value">{movie.description}</span>
                    </div>

                    <div className="footer-row">
                        <span className="label">Maturity rating:</span>
                        <div className="maturity-container">
                            <span className="age-rating">{movie.ageAllow}+</span>
                            <span className="maturity-text">
                                Recommended for ages {movie.ageAllow} and up
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default MovieFooter;