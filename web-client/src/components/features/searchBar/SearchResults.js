import React from 'react';
import { useLocation } from 'react-router-dom';
import MovieList from '../../common/MovieList/MovieList';
import Navbar from '../../common/Navbar/Navbar';
import Footer from '../../common/Footer/Footer';

const SearchResults = () => {
  const location = useLocation();
  const { searchQuery, movies } = location.state || {};
  return (
    <div className="search-results-container">
      <Navbar/>
      <h1>Search Results for "{searchQuery}"</h1>
      {movies && movies.length > 0 ? (
        <MovieList 
          title={`Search Results (${movies.length} movies)`} 
          movies={movies} 
        />
      ) : (
        <p>No movies found matching your search.</p>
      )}
      <Footer/>
    </div>
  );
};

export default SearchResults;