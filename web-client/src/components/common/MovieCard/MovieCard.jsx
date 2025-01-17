import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './MovieCard.css';

const MovieCard = ({ src }) => {
    return (
      <div className="movie-card">
        {/* Image container */}
        <div className="image-container">
          <img
            src={src}
            className="movie-image"
          />
        </div>
        
        {/* Gradient overlay */}
        <div className="gradient-overlay" />
        
        
        {/* Hover overlay */}
        <div className="hover-overlay" />
      </div>
    );
  };

export default MovieCard;