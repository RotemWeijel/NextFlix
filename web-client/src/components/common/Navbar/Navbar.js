import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useTheme } from '../../../hooks/useTheme';
import { Button } from '../Button/Button';
import styles from './Navbar.module.css';

export const Navbar = ({ isLoggedIn, userProfile, onLogout }) => {
  const { colors, isDarkMode, toggleTheme } = useTheme();
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const navigate = useNavigate();  // Add this hook

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
        <div className={styles.logoSection}>
          <Link to="/">
            <img 
              src="/netflix-logo.svg" 
              alt="Netflix Logo" 
              className={styles.logo}
            />
          </Link>
        </div>

        {/* Navigation Links */}
        {isLoggedIn && (
          <div className={styles.navLinks}>
            <Link to="/browse">Home</Link>
            <Link to="/series">TV Shows</Link>
            <Link to="/movies">Movies</Link>
            <Link to="/new">New & Popular</Link>
            <Link to="/my-list">My List</Link>
          </div>
        )}

        {/* Right Section */}
        <div className={styles.rightSection}>
          {/* Theme Toggle */}
          <button 
            onClick={toggleTheme}
            className={styles.themeToggle}
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
                  src={userProfile.avatar || '/default-avatar.png'} 
                  alt="Profile" 
                  className={styles.profileImage}
                />
                {isMenuOpen && (
                  <div className={styles.dropdownMenu}>
                    <Link to="/profile">Profile</Link>
                    <Link to="/account">Account</Link>
                    <button onClick={onLogout}>Sign Out</button>
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