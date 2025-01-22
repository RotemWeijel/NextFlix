import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { getStoredToken, getStoredUser } from '../../utils/auth';
import LoadingSpinner from '../common/LoadingSpinner/LoadingSpinner';

// Route wrapper for authenticated users
export const ProtectedRoute = () => {
    const token = getStoredToken();
    const user = getStoredUser();
  
    // If there's no token, redirect to login
    if (!token || !user) {
      return <Navigate to="/" />;
    }
  
    return <Outlet />;
  };

// Route wrapper specifically for admin users
export const AdminRoute = () => {
    const token = getStoredToken();
    const user = getStoredUser();
  
    // If there's no token or user isn't admin, redirect
    if (!token || !user) {
      return <Navigate to="/login" />;
    }
  
    if (!user.isAdmin) {
      return <Navigate to="/browse" />;
    }
  
    return <Outlet />;
};