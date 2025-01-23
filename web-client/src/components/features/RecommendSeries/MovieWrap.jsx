
import React from 'react';
import './MovieWrap.css';

const MovieWrap = ({ movie }) => {
  return (
    <div className="movie-wrap-card">
      <div className="movie-wrap-container">
        <img src={movie.imageUrl} className="card-img-top" alt={movie.name} />
      </div>
      <div className="card-body">
        <span className="Title">{movie.name} </span>
        <div className="movie-metadata">

          <div className="metadata-group">
            <span className="badge-rating">{movie.ageAllow} +</span>
            <span className="year-text">{movie.releaseYear}</span>
            {movie.duration && <span className="duration">{movie.duration}</span>}
            <button className="plus-button" onClick={() => { }}>+</button>
          </div>
        </div>
        <p className="movie-description">{movie.description}</p>
      </div>
    </div>
  );
};

export default MovieWrap;

