import React, { useState, useEffect } from "react";
import MovieWrap from "./MovieWrap"
import "./RecommendSeries.css";
import { getStoredToken, createAuthHeaders } from '../../../utils/auth';
import { useNavigate } from "react-router-dom";
const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:4000';


const RecommendSeries = ({ movieId }) => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [recommendations, setRecommendations] = useState();
  const navigate = useNavigate()
  const checkAuthAndFetchCategories = async () => {
    if (!getStoredToken()) {
      navigate('/login');
    }
  }
  const fetchRandomMovies = async () => {
    try {
      const headers = {
        ...createAuthHeaders(),
        'Content-Type': 'application/json'
      };
      const response = await fetch(`${API_BASE_URL}/api/movies`, {
        headers: headers
      });
      const data = await response.json();
      const allMovies = data.reduce((acc, category) => {
        return [...acc, ...category.movies];
      }, []);

      // Get 12 random movies
      const randomMovies = allMovies
        .sort(() => Math.random() - 0.5)
        .slice(0, 12);

      setRecommendations(randomMovies);
    } catch (error) {
      setError(error);
    }
  };
  useEffect(() => {
    const fetchMovie = async () => {
      try {
        const actualId = typeof movieId === 'object' ? movieId.movie : movieId;
        const headers = {
          ...createAuthHeaders(),
          'Content-Type': 'application/json'
        };
        const url = `${API_BASE_URL}/api/movies/${actualId}/recommend`;

        const res = await fetch(url, {
          headers: headers
        });

        if (!res.ok) {
          throw new Error(`HTTP error! status: ${res.status}`);
        }
        const data = await res.json();
        if (!data || data.length === 0) {
          await fetchRandomMovies();
        } else {
          setRecommendations(data);
        }
      } catch (error) {
        await fetchRandomMovies();
      }
    };

    if (movieId) {
      fetchMovie();
    }
  }, [movieId]);


  if (loading) return <div className="recommend-series-container">Loading recommendations...</div>;
  if (error) return <div className="recommend-series-container">Error: {error}</div>;

  return (
    <div className="recommend-series-container">
      <h2 className="recommend-title">More Like This</h2>
      <div className="movies-grid">
        {recommendations.map((movie) => (
          <MovieWrap key={movie.id} movie={movie} />
        ))}
      </div>
    </div>
  );
};
export default RecommendSeries;
