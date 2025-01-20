import React, { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import LoadingSpinner from '../../../components/common/LoadingSpinner/LoadingSpinner';
import styles from './RegistrationSuccess.module.css';

const RegistrationSuccess = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { username, password } = location.state || {};

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
    // TODO: Implement actual login logic here
    // For now, just navigate to home
    navigate('/home');
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
          <h1 className={styles.title}>Welcome to Streamflix!</h1>
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