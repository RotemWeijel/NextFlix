import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useTheme } from '../../../hooks/useTheme';
import { Button } from '../Button/Button';
import { getStoredUser, clearAuthData, createAuthHeaders } from '../../../utils/auth';
import styles from './Navbar.module.css';

export const Navbar = () => {
  const { colors, isDarkMode, toggleTheme } = useTheme();
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [imageError, setImageError] = useState(false);
  const [isSearchOpen, setIsSearchOpen] = useState(false);
  const [user, setUser] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');
  const navigate = useNavigate();

  const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:4000';


  // Initialize user data from storage
  useEffect(() => {
    const storedUser = getStoredUser();
    setUser(storedUser);
  }, []);

  const handleLogout = () => {
    // Clear auth data from storage
    clearAuthData();
    // Clear user state
    setUser(null);
    // Close dropdown menu
    setIsMenuOpen(false);
    // Redirect to login page
    navigate('/login');
  };


  const handleSearch = async (e) => {
    e.preventDefault();
    if (!searchQuery.trim()) return;
  
    try {
      console.log('Searching for:', searchQuery);
      const response = await fetch(`${API_BASE_URL}/api/movies/search/${searchQuery}`, {
        headers: {
          ...createAuthHeaders(),
          'Content-Type': 'application/json'
        }
      });
  
      const data = await response.json();
      console.log('Full search response:', data); 
      console.log('Movies:', data.movies || data.data || data); 
      
      navigate('/movies/search', { 
        state: { 
          searchQuery, 
          movies: data.movies || data.data || data 
        } 
      });
    } catch (error) {
      console.error('Search error:', error);
    }
  };
  

  const handleImageError = () => {
    setImageError(true);
  };

  // Check if user is logged in
  const isLoggedIn = !!user;

  return (
    <nav
      className={styles.navbar}
      style={{
        '--navbar-bg': colors.background.primary,
        '--navbar-text': colors.text.primary,
        '--navbar-border': colors.border
      }}
    >
      <div className={styles.navContent}>
        {/* Logo */}
        <Link to="/movies/browse" className={styles.logoSection}>
          <img
            src="/NextFlix_icon.png"
            alt="NextFlix Logo"
            className={styles.logo}
          />
        </Link>

        {/* Navigation Links */}
        {isLoggedIn && (
          <div className={styles.navLinks}>
            <Link to="/movies/browse">Home</Link>
            <Link to="/movies/browse">TV Shows</Link>
            <Link to="/movies/browse">Movies</Link>
            {user?.isAdmin && (
              <Link to="/admin/categories" className={styles.adminLink}>Admin</Link>
            )}
            </div>
        )}

        {/* Right Section */}
        <div className={styles.rightSection}>
          {/* Search Icon */}
          <div className={styles.searchContainer}>
          {isSearchOpen ? (
          <form onSubmit={handleSearch} className={styles.searchBox}>
            <svg
              viewBox="0 0 24 24"
              className={styles.searchIcon}
              onClick={() => setIsSearchOpen(false)}
            >
              <path fill="currentColor" d="M15.5 14h-.79l-.28-.27A6.471 6.471 0 0 0 16 9.5 6.5 6.5 0 1 0 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z" />
            </svg>
            <input
              type="text"
              placeholder="Titles, people, genres"
              className={styles.searchInput}
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              autoFocus
            />
            <Button type="submit" className={styles.searchSubmit}>Search</Button>
          </form>
        ) : (
          <svg
            viewBox="0 0 24 24"
            className={styles.searchIcon}
            onClick={() => setIsSearchOpen(true)}
          >
            <path fill="currentColor" d="M15.5 14h-.79l-.28-.27A6.471 6.471 0 0 0 16 9.5 6.5 6.5 0 1 0 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z" />
          </svg>
        )}
      </div>

          {/* Theme Toggle */}
          <button
            onClick={toggleTheme}
            className={styles.themeToggle}
            aria-label="Toggle theme"
          >
            {isDarkMode ? '☀️' : '🌙'}
          </button>

          {isLoggedIn ? (
            <div className={styles.userSection}>
              <div
                className={styles.profileDropdown}
                onClick={() => setIsMenuOpen(!isMenuOpen)}
              >
                <img
                  src={!imageError ? (user?.picture || '/default-avatar.png') : '/default-avatar.png'}
                  alt={`${user?.full_name || 'User'}'s profile`}
                  className={styles.profileImage}
                  onError={handleImageError}
                />
                {isMenuOpen && (
                  <div 
                    className={styles.dropdownMenu}
                    style={{
                      color: colors.text.primary,
                      backgroundColor: colors.background.secondary
                    }}
                  >
                    <div className={styles.userInfo}>
                      <table className={styles.userTable} cellPadding={5} align='center'>
                        <tbody>
                          <tr>
                            <td className={styles.userLabel}>Username:</td>
                            <td className={styles.userName}>{user?.username}</td>
                          </tr>
                          <tr>
                            <td className={styles.userLabel}>Display Name:</td>
                            <td className={styles.userName}>{user?.full_name}</td>
                          </tr>
                          <tr>
                            <td className={styles.userLabel}>Role:</td>
                            <td className={styles.userRole}>{user?.isAdmin ? 'Administrator' : 'User'}</td>
                          </tr>
                        </tbody>
                      </table>
                      <img
                        src={!imageError ? (user?.picture || '/default-avatar.png') : '/default-avatar.png'}
                        alt="Profile"
                        className={styles.userInfoImage}
                        onError={handleImageError}
                        align='center'
                      />
                    </div>
                    <button
                      onClick={handleLogout}
                      className={styles.signOutButton}
                    >
                      Sign Out
                    </button>
                  </div>
                )}
              </div>
            </div>
          ) : (
            <div className={styles.authButtons}>
              <Button
                variant="secondary"
                size="small"
                onClick={() => navigate('/login')}
              >
                Sign In
              </Button>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;