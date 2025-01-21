import React, { useState, useEffect } from "react";
import MovieWrap from "./MovieWrap"
import "./RecommendSeries.css";

// Default data for demonstration
const DEFAULT_RECOMMENDATIONS = [
  {
    id: 1,
    title: "Caliphate",
    image: "/favicon.ico",
    year: 2020,
    duration: "8:56",
    description: "An impending ISIS attack on Sweden entangles a group of women, including a mother in a bind, a spirited student and an ambitious cop.",
    rating: "16+"
  },
  {
    id: 2,
    title: "Hit & Run",
    image: "/api/placeholder/220/120",
    year: 2021,
    duration: 9,
    description: "A former special ops soldier has settled into a happy family life — until tragedy thrusts him back into a world of secret plots and deadly threats.",
    rating: "16+"
  },
  {
    id: 3,
    title: "The Girl From Oslo",
    image: "/api/placeholder/220/120",
    year: 2021,
    duration: 10,
    description: "In this suspenseful thriller series, a diplomat attempts to save her daughter who's been kidnapped by terrorists.",
    rating: "16+"
  }, {
    id: 4,
    title: "The Girl From Oslo",
    image: "/api/placeholder/220/120",
    year: 2021,
    duration: 10,
    description: "In this suspenseful thriller series, a diplomat attempts to save her daughter who's been kidnapped by terrorists.",
    rating: "16+"
  }, {
    id: 5,
    title: "The Girl From Oslo",
    image: "/api/placeholder/220/120",
    year: 2021,
    duration: 10,
    description: "In this suspenseful thriller series, a diplomat attempts to save her daughter who's been kidnapped by terrorists.",
    rating: "16+"
  }, {
    id: 6,
    title: "The Girl From Oslo",
    image: "/api/placeholder/220/120",
    year: 2021,
    episodes: 10,
    description: "In this suspenseful thriller series, a diplomat attempts to save her daughter who's been kidnapped by terrorists.",
    rating: "16+"
  }, {
    id: 7,
    title: "The Girl From Oslo",
    image: "/api/placeholder/220/120",
    year: 2021,
    episodes: 10,
    description: "In this suspenseful thriller series, a diplomat attempts to save her daughter who's been kidnapped by terrorists.",
    rating: "16+"
  }, {
    id: 8,
    title: "The Girl From Oslo",
    image: "/api/placeholder/220/120",
    year: 2021,
    episodes: 10,
    description: "In this suspenseful thriller series, a diplomat attempts to save her daughter who's been kidnapped by terrorists.",
    rating: "16+"
  }, {
    id: 8,
    title: "The Girl From Oslo",
    image: "/api/placeholder/220/120",
    year: 2021,
    episodes: 10,
    description: "In this suspenseful thriller series, a diplomat attempts to save her daughter who's been kidnapped by terrorists.",
    rating: "16+"
  }
];

const RecommendSeries = ({ movieId }) => {
  const [recommendations, setRecommendations] = useState(DEFAULT_RECOMMENDATIONS);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchRecommendations = async () => {
      try {
        setLoading(true);
        // Uncomment when server is ready
        // const response = await fetch(`http://localhost:5000/api/movies/${movieId}/recommend`);
        // if (!response.ok) throw new Error('Failed to fetch recommendations');
        // const data = await response.json();
        // setRecommendations(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    // fetchRecommendations();
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
