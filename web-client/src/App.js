import { ThemeProvider, useTheme } from './hooks/useTheme';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Landing from './screens/auth/Landing/Landing';
import Login from './screens/auth/Login/Login';
import Register from './screens/auth/Register/Register';
import TestComponents from './TestComponents';
import './App.css';

function App() {
  return (
    <ThemeProvider>
      <Router>
        <AppContent />
      </Router>
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
        <Route path="/" element={<Landing />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register normalImage="/images/Register/3D-glasses.jpg" sunglassesImage="/images/Register/sunglasses.png" />} />
        <Route path="/test" element={<TestComponents />} />
      </Routes>
    </div>
  );
}

export default App;
