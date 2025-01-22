import React, { useState } from 'react';
import styles from './SearchBar.module.css';

const SearchBar = ({ onSearch }) => {
  const [query, setQuery] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [movies, setMovies] = useState([]);
  const [hasSearched, setHasSearched] = useState(false); // Track if the user searched

  const fetchMovies = async (searchQuery) => {
    if (!searchQuery.trim()) return;

    setIsLoading(true);
    setHasSearched(true); // Mark that a search has happened
    try {
      const response = await fetch(`http://localhost:4000/api/movies/search/${encodeURIComponent(searchQuery)}`);
      const data = await response.json();
      setMovies(data);
      onSearch(data);
    } catch (error) {
      console.error('Error fetching movies:', error);
      setMovies([]);
      onSearch([]);
    } finally {
      setIsLoading(false);
    }
  };

  const handleSearch = (e) => {
    e.preventDefault();
    fetchMovies(query);
  };

  return (
    <div className={styles['search-container']}>
      <form onSubmit={handleSearch} className={styles['search-form']}>
        <input
          type="text"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Search for movies..."
          className={styles['input-field']}
        />
        <button type="submit" className={styles['search-button']}>
          Search
        </button>
      </form>

      {isLoading && <div className="text-center text-gray-400">Loading...</div>}

      {/* Show "No movies found" message only after search has been made */}
      {!isLoading && hasSearched && movies.length === 0 && (
        <div className={styles['no-movies']}>No movies found for "{query}"</div>
      )}
    </div>
  );
};

export default SearchBar;
