import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './MovieCard.css';
import { useNavigate } from 'react-router-dom';
const MovieCard = ({ src, name, movieId }) => {

  const [imageError, setImageError] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const defaultImage = "/netflix.png";
  const navigate = useNavigate();
  const getImageUrl = () => {
    if (!src || src === '') {
      return defaultImage;
    }

    if (imageError) {
      return defaultImage;
    }

    return src;
  };
  const handleclick = () => {
    navigate(`/MovieDetails/${movieId}`)
  }

  return (
    <div className="movie-card" onClick={handleclick}>
      <div className="image-container">
        <img
          src={getImageUrl()}
          className={`movie-image ${isLoading ? 'loading' : ''}`}
          alt={name}
          onError={() => {
            console.log('Image failed to load:', src);
            setImageError(true);
            setIsLoading(false);
          }}
          onLoad={() => setIsLoading(false)}

        />
      </div>

      <div className="gradient-overlay" />
      <div className="hover-overlay" />
    </div>
  );
};

export default MovieCard;
