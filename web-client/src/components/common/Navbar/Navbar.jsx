import React, { useState } from 'react';
import { useTheme } from '../../../hooks/useTheme';
import { Button } from '../Button/Button';
import styles from './Navbar.module.css';

export const Navbar = ({ isLoggedIn, userProfile, onLogout }) => {
  const { colors, isDarkMode, toggleTheme } = useTheme();
  const [isMenuOpen, setIsMenuOpen] = useState(false);

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
          <img 
            src="/netflix-logo.svg" 
            alt="Netflix Logo" 
            className={styles.logo}
          />
        </div>

        {/* Navigation Links */}
        {isLoggedIn && (
          <div className={styles.navLinks}>
            <a href="/browse">Home</a>
            <a href="/series">TV Shows</a>
            <a href="/movies">Movies</a>
            <a href="/new">New & Popular</a>
            <a href="/my-list">My List</a>
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
                    <a href="/profile">Profile</a>
                    <a href="/account">Account</a>
                    <button onClick={onLogout}>Sign Out</button>
                  </div>
                )}
              </div>
            </div>
          ) : (
            <div className={styles.authButtons}>
              <Button variant="secondary" size="small" href="/login">
                Sign In
              </Button>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
};
