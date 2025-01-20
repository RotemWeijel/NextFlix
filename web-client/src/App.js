import { ThemeProvider, useTheme } from './hooks/useTheme';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import { ProtectedRoute, AdminRoute } from './components/routing/ProtectedRoutes';
import Landing from './screens/auth/Landing/Landing';
import Login from './screens/auth/Login/Login';
import Register from './screens/auth/Register/Register';
import RegistrationSuccess from './screens/auth/Register/RegistrationSuccess';
import TestComponents from './TestComponents';
import './App.css';

function App() {
  return (
  
    <ThemeProvider>
      <AuthProvider>
        <Router>
          <AppContent />
        </Router>
      </AuthProvider>
    </ThemeProvider>
    
  );
}

function AppContent() {
  const { colors } = useTheme();

  return (
    <div 
      style={{
        '--app-bg': colors.background.primary,
        '--app-text': colors.text.primary,
        minHeight: '100vh',
        backgroundColor: colors.background.primary,
        color: colors.text.primary
      }}
    >
      <Routes>
        {/* Public routes */}
        <Route path="/" element={<Landing />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register normalImage="/images/Register/3D-glasses.jpg" sunglassesImage="/images/Register/sunglasses.png" />} />

        {/* Protected routes for authenticated users */}
        <Route element={<ProtectedRoute />}>
          <Route path="/registration-success" element={<RegistrationSuccess />} />
          <Route path="/test" element={<TestComponents />} />
        </Route>

        {/* Protected routes for admin users */}
        <Route element={<AdminRoute />}>
          {/* <Route path="/admin/dashboard" element={<AdminDashboard />} />
          <Route path="/admin/movies" element={<MovieManagement />} />
          <Route path="/admin/categories" element={<CategoryManagement />} /> */}
        </Route>
      </Routes>
    </div>
  );
}

export default App;
