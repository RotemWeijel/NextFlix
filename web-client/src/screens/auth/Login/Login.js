import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import Input from '../../../components/common/Input/Input';
import Button from '../../../components/common/Button/Button';
import LoadingSpinner from '../../../components/common/LoadingSpinner/LoadingSpinner';
import { setAuthData, getStoredToken } from '../../../utils/auth';
import styles from './Login.module.css';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:4000';

const Login = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: '',
    password: ''
  });
  const [errors, setErrors] = useState({});
  const [isLoading, setIsLoading] = useState(false);
  const [generalError, setGeneralError] = useState('');

  // Check if user is already logged in
  useEffect(() => {
    const token = getStoredToken();
    if (token) {
      navigate('/browse');
    }
  }, [navigate]);

  const validateForm = () => {
    const newErrors = {};
    if (!formData.username.trim()) {
      newErrors.username = 'Username is required';
    }
    if (!formData.password) {
      newErrors.password = 'Password is required';
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setGeneralError('');
    
    if (!validateForm()) return;

    setIsLoading(true);
    try {
      const response = await fetch(`${API_BASE_URL}/api/tokens`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.error || 'Login failed');
      }

      // Store authentication data
      localStorage.setItem('token', data.token);
      localStorage.setItem('user', JSON.stringify(data.user));

      // Store authentication data using the utility function
      setAuthData(data.token, data.user);

      
      navigate('/browse');
      

    } catch (error) {
      setGeneralError(error.message || 'Failed to sign in. Please check your credentials.');
      setErrors({});
    } finally {
      setIsLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    // Clear errors when user starts typing
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.formCard}>
        <h1 className={styles.title}>Sign In</h1>
        {generalError && (
          <div className={styles.errorMessage}>
            {generalError}
          </div>
        )}
        <form onSubmit={handleSubmit} className={styles.form}>
          <Input
            label="Username"
            type="text"
            name="username"
            value={formData.username}
            onChange={handleChange}
            error={errors.username}
            disabled={isLoading}
            required
          />
          <Input
            label="Password"
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            error={errors.password}
            disabled={isLoading}
            required
          />
          <Button 
            type="submit" 
            variant="primary" 
            fullWidth 
            disabled={isLoading}
          >
            {isLoading ? <LoadingSpinner /> : 'Sign In'}
          </Button>
        </form>
        <p className={styles.registerLink}>
          New to NextFlix? <Link to="/register">Sign up now</Link>
        </p>
      </div>
    </div>
  );
};

export default Login;