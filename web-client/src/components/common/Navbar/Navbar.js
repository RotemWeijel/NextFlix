import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useTheme } from '../../../hooks/useTheme';
import { Button } from '../Button/Button';
import { getStoredUser, clearAuthData } from '../../../utils/auth';
import styles from './Navbar.module.css';

export const Navbar = () => {
  const { colors, isDarkMode, toggleTheme } = useTheme();
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [imageError, setImageError] = useState(false);
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

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
        <Link to="/" className={styles.logoSection}>
          <img 
            src="/NextFlix_icon.png" 
            alt="NextFlix Logo" 
            className={styles.logo}
          />
        </Link>

        {/* Navigation Links */}
        {isLoggedIn && (
          <div className={styles.navLinks}>
            <Link to="/browse">Home</Link>
            <Link to="/series">TV Shows</Link>
            <Link to="/movies">Movies</Link>
            <Link to="/new">New & Popular</Link>
            <Link to="/my-list">My List</Link>
            {user?.isAdmin && (
              <Link to="/admin/categories" className={styles.adminLink}>Admin</Link>
            )}
          </div>
        )}

        {/* Right Section */}
        <div className={styles.rightSection}>
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
                  <div className={styles.dropdownMenu}>
                  <div className={styles.userInfo}>
                    <table className={styles.userTable}>
                      <tbody>
                        <tr>
                          <td className={styles.userLabel}>Username:</td>
                          <td className={styles.userName}>{user?.username}</td>
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
                    />
                  </div>
                    <Link to="/profile" onClick={() => setIsMenuOpen(false)}>Profile</Link>
                    <Link to="/account" onClick={() => setIsMenuOpen(false)}>Account</Link>
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