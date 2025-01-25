import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import LoadingSpinner from '../../../components/common/LoadingSpinner/LoadingSpinner';
import { setAuthData } from '../../../utils/auth';
import styles from './RegistrationSuccess.module.css';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:4000';

const RegistrationSuccess = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { username, password } = location.state || {};
  const [error, setError] = useState('');

  useEffect(() => {
    // Verify that we have the required credentials
    if (!username || !password) {
      navigate('/register');
      return;
    }

    // Set timer for auto-login attempt
    const timer = setTimeout(() => {
      handleAutoLogin();
    }, 4500);

    return () => clearTimeout(timer);
  }, [username, password, navigate]);

  const handleAutoLogin = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/tokens`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username,
          password
        }),
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.error || 'Auto-login failed');
      }

      // Store authentication data
      setAuthData(data.token, data.user);

      // Redirect based on admin status
      navigate('/movies/browse');
      
    } catch (error) {
      setError('Auto-login failed. Please try logging in manually.');
      // Redirect to login page after a short delay if auto-login fails
      setTimeout(() => {
        navigate('/login');
      }, 2000);
    }
  };

  return (
    <div className={styles.container}>
      <img
        src="/images/Register/registration-success.jpeg"
        alt=""
        className={styles.backgroundImage}
      />
      <div className={styles.contentWrapper}>
        <div className={styles.content}>
          <h1 className={styles.title}>Welcome to NextFlix!</h1>
          <p className={styles.message}>
            Registration successful! Logging you in...
          </p>
          <div className={styles.loadingIndicator}>
            <LoadingSpinner size="medium" />
          </div>
          <p className={styles.countdown}>
              Just a few moments until your streaming journey begins...
            </p>
        </div>
      </div>
    </div>
  );
};

export default RegistrationSuccess;