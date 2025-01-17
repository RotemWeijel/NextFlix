import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import { Navbar } from '../../components/common/Navbar/Navbar';
import VideoPlayer from '../../components/features/player/VideoPlayer';
import MovieList from '../../components/common/MovieList/MovieList'
import { Footer } from '../../components/common/Footer/Footer';
import { LoadingSpinner } from '../../components/common/LoadingSpinner/LoadingSpinner';
import { ThemeProvider } from '../../hooks/useTheme';
import 'bootstrap/dist/css/bootstrap.min.css';
import './BrowseScreen.css';
import MoviesPage from '../../components/features/MoviesByCategory/MoviesPage'

const BrowseScreen = ({ }) => {
    const navigate = useNavigate()
    const [movies, setMovies] = useState([]);
    const [loading, setLoading] = useState(true);
    const [randomMovie, setRandomMovie] = useState(null);
    const videoRef = useRef(null);
    const [isPlaying, setIsPlaying] = useState(false);
    const [isMuted, setIsMuted] = useState(true);
    const [isHovered, setIsHovered] = useState(false);

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
    useEffect(() => {
        let autoplayTimer;
        if (isHovered && videoRef.current && !isPlaying) {
            autoplayTimer = setTimeout(() => {
                videoRef.current.play();
                setIsPlaying(true);
            }, 100);
        }
        return () => {
            if (autoplayTimer) clearTimeout(autoplayTimer);
        };
    }, [isHovered]);
    const handlePlayPause = () => {
        navigate('/player', { state: { movieId: randomMovie?.id } });
    };

    const handleMuteToggle = () => {
        if (videoRef.current) {
            if (isMuted) {
                videoRef.current.unmute();
            } else {
                videoRef.current.mute();
            }
            setIsMuted(!isMuted);
        }
    };
    const handleMouseEnter = () => {
        setIsHovered(true);
    };

    const handleMouseLeave = () => {
        setIsHovered(false);
        if (videoRef.current && isPlaying) {
            videoRef.current.pause();
            setIsPlaying(false);
        }
    };

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
                        <div className="hero-section">
                            <div
                                className="hero-video-wrapper"
                                onMouseEnter={handleMouseEnter}
                                onMouseLeave={handleMouseLeave}
                            >
                                <div className="hero-video-wrapper">
                                    <VideoPlayer
                                        ref={videoRef}
                                        videoUrl={"/video_480.mp4"}
                                        onPlayPauseChange={setIsPlaying}
                                        onMuteChange={setIsMuted}
                                    />
                                    <div className="video-controls">
                                        <button
                                            onClick={handlePlayPause}
                                        >
                                            {isPlaying ? '⏸' : '▶'}
                                        </button>
                                        <button
                                            onClick={handleMuteToggle}
                                        >
                                            {isMuted ? '🔈' : '🔊'}
                                        </button>
                                    </div>
                                </div>
                            </div> </div>
                        <MoviesPage />
                        <Footer />
                    </>
                )}
            </div>
        </ThemeProvider>
    );
};
export default BrowseScreen