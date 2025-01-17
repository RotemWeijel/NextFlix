import React, { useState, useRef, useEffect } from "react";
import { Navbar } from '../../components/common/Navbar/Navbar';
import VideoPlayer from '../../components/features/player/VideoPlayer';
import MovieList from '../../components/common/MovieList/MovieList'
import { Footer } from '../../components/common/Footer/Footer';
import { LoadingSpinner } from '../../components/common/LoadingSpinner/LoadingSpinner';
import { ThemeProvider } from '../../hooks/useTheme';

const BrowseScreen = ({ }) => {
    const [movies, setMovies] = useState([]);
    const [loading, setLoading] = useState(true);
    const [randomMovie, setRandomMovie] = useState(null);
    const videoRef = useRef(null);

    useEffect(() => {
        const fetchMovies = async () => {
            try {
                const response = await fetch('http://localhost:3000/api/movies');
                const data = await response.json();
                setMovies(data);
                const randomIndex = Math.floor(Math.random() * data.length);
                setRandomMovie(data[randomIndex]);

                setLoading(false);
            } catch (error) {
                console.error('Error fetching movies:', error);
                setLoading(false);
            }
        };
        fetchMovies();
    }, []);

    return (
        <ThemeProvider>
            <div className="App">
                <Navbar isLoggedIn={true} userProfile={{ avatar: '/user.png' }} onLogout={() => { }} />
                {loading ? (
                    <LoadingSpinner />
                ) : (
                    randomMovie && (
                        <VideoPlayer
                            videoUrl={"/video_480.mp4"}
                            ref={videoRef}
                        />

                    )
                )}
            </div>
        </ThemeProvider>
    )
}
export default BrowseScreen