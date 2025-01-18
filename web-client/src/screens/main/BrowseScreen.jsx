import React, { useState, useEffect } from "react";
import { Navbar } from '../../components/common/Navbar/Navbar';
import { Footer } from '../../components/common/Footer/Footer';
import { LoadingSpinner } from '../../components/common/LoadingSpinner/LoadingSpinner';
import { ThemeProvider } from '../../hooks/useTheme';
import PlayerHome from '../../components/features/VideoPlayerHome/PlayerHome'
import MoviesPage from '../../components/features/MoviesByCategory/MoviesPage';
import 'bootstrap/dist/css/bootstrap.min.css';
import './BrowseScreen.css';

const BrowseScreen = () => {
    const { loading, error } = PlayerHome();
    if (error) {
        return <div>Error loading movies: {error.message}</div>;
    }

    return (
        <ThemeProvider>
            <div className="App">
                <Navbar
                    isLoggedIn={true}
                    userProfile={{ avatar: '/user.png' }}
                    onLogout={() => { }}
                />
                {loading ? (
                    <LoadingSpinner />
                ) : (
                    <>
                        <PlayerHome />
                        <MoviesPage />
                        <Footer />
                    </>
                )}
            </div>
        </ThemeProvider>
    );
};

export default BrowseScreen;