import { ThemeProvider, useTheme } from './hooks/useTheme';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import { ProtectedRoute, AdminRoute } from './components/routing/ProtectedRoutes';
import Landing from './screens/auth/Landing/Landing';
import Login from './screens/auth/Login/Login';
import Register from './screens/auth/Register/Register';
import RegistrationSuccess from './screens/auth/Register/RegistrationSuccess';
import BrowseScreen from './screens/main/BrowseScreen';
import MovieDetailsScreen from './screens/main/MovieDetailsScreen';
import PlayerScreen from './screens/main/PlayerScreen';
import TestComponents from './TestComponents';
import CategoryManagement from '../src/screens/admin/CategoryManagement'
import MovieManagement from './screens/admin/MovieManagement'; 
import SearchResults from './components/features/searchBar/SearchResults';
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
  
  {/* Auth routes */}
  <Route path="/">
    <Route path="login" element={<Login />} />
    <Route path="register" element={<Register normalImage="/images/Register/3D-glasses.jpg" sunglassesImage="/images/Register/sunglasses.png" />} />
    <Route path="registration-success" element={<RegistrationSuccess />} />
  </Route>

  {/* Protected user routes */}
  <Route element={<ProtectedRoute />}>          
    <Route path="/movies">
      <Route path="browse" element={<BrowseScreen />} />
      <Route path=":movie/details" element={<MovieDetailsScreen />} />
      <Route path=":movie/play" element={<PlayerScreen />} />
      <Route path="search" element={<SearchResults />} />
    </Route>
    <Route path="/test" element={<TestComponents />} />
  </Route>

  {/* Protected admin routes */}
  <Route path="/admin" element={<AdminRoute />}>
    <Route path="movies/:id/edit" element={<MovieManagement />} />
    <Route path="categories" element={<CategoryManagement />} />
  </Route>
</Routes>
    </div>
  );
}

export default App;
