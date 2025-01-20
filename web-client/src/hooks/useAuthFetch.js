import { useNavigate } from 'react-router-dom';
import { createAuthHeaders, clearAuthData } from '../utils/auth';

const useAuthFetch = () => {
  const navigate = useNavigate();

  const authFetch = async (url, options = {}) => {
    const headers = {
      ...createAuthHeaders(),
      'Content-Type': 'application/json',
      ...options.headers,
    };

    try {
      const response = await fetch(url, {
        ...options,
        headers,
      });

      // If token is invalid/expired, logout and redirect to login
      if (response.status === 401) {
        clearAuthData();
        navigate('/login');
        throw new Error('Session expired. Please login again.');
      }

      return response;
    } catch (error) {
      throw error;
    }
  };

  return authFetch;
};

export default useAuthFetch;