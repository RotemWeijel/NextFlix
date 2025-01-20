import React, { useState, useEffect } from "react";
import { Navbar } from '../../components/common/Navbar/Navbar';
import { Footer } from '../../components/common/Footer/Footer';
import { LoadingSpinner } from '../../components/common/LoadingSpinner/LoadingSpinner';
import { ThemeProvider } from '../../hooks/useTheme';
import PlayerHome from '../../components/features/VideoPlayerHome/PlayerHome'
import MoviesPage from '../../components/features/MoviesByCategory/MoviesPage';
import 'bootstrap/dist/css/bootstrap.min.css';
import './BrowseScreen.css';

const BrowseScreen = ({ tokenUser }) => {
    const [error, setError] = useState(null);

    return (
        <ThemeProvider>
            <div className="App">
                <Navbar
                    isLoggedIn={true}
                    userProfile={{ avatar: '/user.png' }}
                    onLogout={() => { }}
                />
                <PlayerHome tokenUser={tokenUser} />
                <MoviesPage tokenUser={tokenUser} />
                <Footer />
            </div>
        </ThemeProvider>
    );
};

export default BrowseScreen;