
import React from 'react';
import './MovieWrap.css';

const MovieWrap = ({ movie }) => {
  return (
    <div className="movie-card">
      <div className="card-img-container">
        <img src={movie.image} className="card-img-top" alt={movie.title} />
      </div>
      <div className="card-body">
        <div className="movie-metadata">
          <div className="metadata-group">
            <span className="badge-rating">{movie.rating}</span>
            <span className="year-text">{movie.year}</span>
            {movie.duration && <span className="duration">{movie.duration}</span>}
            <button className="plus-button" onClick={() => {}}>+</button>
          </div>
          </div>
        <p className="movie-description">{movie.description}</p>
      </div>
    </div>
  );
};

export default MovieWrap;

