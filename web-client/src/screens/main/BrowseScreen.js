import React, { useState, useEffect } from "react";
import { Navbar } from '../../components/common/Navbar/Navbar';
import { Footer } from '../../components/common/Footer/Footer';
import { ThemeProvider } from '../../hooks/useTheme';
import PlayerHome from '../../components/features/VideoPlayerHome/PlayerHome'
import MoviesPage from '../../components/features/MoviesByCategory/MoviesPage';
import { useNavigate } from "react-router-dom";
import { getStoredToken, createAuthHeaders } from '../../utils/auth'
import './BrowseScreen.css';
const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:4000';
const BrowseScreen = ({ }) => {
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    useEffect(() => {
        if (!getStoredToken()) {
            navigate('/login');
        }
    }, []);

    return (
        <ThemeProvider>
            <div className="App">
                <Navbar
                    isLoggedIn={true}
                    userProfile={{ avatar: '/user.png' }}
                    onLogout={() => { }}
                />
                <PlayerHome />
                <MoviesPage />
                <Footer />
            </div>
        </ThemeProvider>
    );
};

export default BrowseScreen;