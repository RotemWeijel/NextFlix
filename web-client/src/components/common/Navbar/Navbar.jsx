import React, { useState } from 'react';
import { useTheme } from '../../../hooks/useTheme';
import { Button } from '../Button/Button';
import styles from './Navbar.module.css';

export const Navbar = ({ isLoggedIn, userProfile, onLogout }) => {
  const { colors, isDarkMode, toggleTheme } = useTheme();
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isSearchOpen, setIsSearchOpen] = useState(false);

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
        {/* Left Section */}
        <div className={styles.leftSection}>
          <img
            src="/netflix-logo.png"
            alt="Netflix"
            className={styles.logo}
          />

          {isLoggedIn && (
            <div className={styles.navLinks}>
              <a href="/browse">Home</a>
              <a href="/series">TV Shows</a>
              <a href="/movies">Movies</a>
              <a href="/new">New & Popular</a>
              <a href="/my-list">My List</a>
            </div>
          )}
        </div>

        {/* Right Section */}
        <div className={styles.rightSection}>
          {/* Search Icon */}
          <div className={styles.searchContainer}>
            {isSearchOpen ? (
              <div className={styles.searchBox}>
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
                  autoFocus
                />
              </div>
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


          {/* User Profile */}
          {isLoggedIn ? (
            <div className={styles.profileContainer}>
              <img
                src={userProfile?.profileImage || '/profile.png'}
                alt="Profile"
                className={styles.profileImage}
                onClick={() => setIsMenuOpen(!isMenuOpen)}
              />
              {isMenuOpen && (
                <div className={styles.dropdownMenu}>
                  <a href="/profile">Profile</a>
                  <a href="/account">Account</a>
                  <button onClick={onLogout}>Sign Out</button>
                </div>
              )}
            </div>

          ) : (
            <Button variant="secondary" size="small" href="/login">
              Sign In
            </Button>
          )}
        </div>
        <button
          onClick={toggleTheme}
          className={styles.themeToggle}
        >
          {isDarkMode ? '☀️' : '🌙'}
        </button>
      </div>
    </nav>
  );
};