import React from 'react';
import './DetailsMovie.css';

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

const DetailsMovie = ({  }) => {
    if (!movie) return null;

    const getAgeRating = (ageAllow) => `${ageAllow}+`;

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
                                {movie.actors.map((actor, index) => (
                                    <React.Fragment key={actor.name}>
                                        {index > 0 && ", "}
                                        {actor.name}
                                    </React.Fragment>
                                ))}
                                <span className="more">, more</span>
                            </span>
                        </div>

                        <div className="metadata-row">
                            <span className="metadata-label">Genres:</span>
                            <span className="metadata-value">
                                {movie.categories.join(", ")}
                            </span>
                        </div>

                        <div className="metadata-row">
                            <span className="metadata-label">This show is:</span>
                            <span className="metadata-value">
                                {movie.traits.join(", ")}
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default DetailsMovie;