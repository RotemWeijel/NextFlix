import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { Navbar } from './components/common/Navbar/Navbar';
import { Footer } from './components/common/Footer/Footer';
import { LoadingSpinner } from './components/common/LoadingSpinner/LoadingSpinner';
import { ThemeProvider } from './hooks/useTheme';
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <ThemeProvider>
      <div className="App">
        <Navbar isLoggedIn={true} userProfile={{ avatar: '/user.png' }} onLogout={() => { }} />
        {/* Other components */}
        <Footer />
      </div>
    </ThemeProvider>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
