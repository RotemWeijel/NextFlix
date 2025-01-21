import React from 'react';
import './MovieFooter.css';

const movie = {
    id: 1,
    title: "Caliphate",
    image: "/favicon.ico",
    releaseYear: 2020,
    duration: "8:56",
    description: "An impending ISIS attack on Sweden entangles a group of women, including a mother in a bind, a spirited student and an ambitious cop.",
    ageAllow: 16,
    actors: [
        { name: "Gizem Erdogan" },
        { name: "Aliette Opheim" },
        { name: "Nora Rios" }
    ],
    categories: [
        "Crime TV Shows",
        "TV Dramas",
        "Scandinavian TV Shows"
    ],
    traits: ["Witty", "Irreverent"]
};

const MovieFooter = ({ }) => {
    return (
        <div className="footer-wrapper">
            <div className="movie-footer">
                <h2 className="footer-title">About {movie.name}</h2>
                <div className="footer-content">
                    {movie.creators && (
                        <div className="footer-row">
                            <span className="label">Creators:</span>
                            <span className="value">{movie.creators.join(', ')}</span>
                        </div>
                    )}
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
                        <span className="label">This show is:</span>
                        <span className="value">
                            {movie.description.split('.')[0]}
                        </span>
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