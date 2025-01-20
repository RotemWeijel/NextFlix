import React, { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import VideoPlayer from '../../common/player/VideoPlayer';
import './PlayerHome.css';

const PlayerHome = () => {
    const navigate = useNavigate();
    const videoRef = useRef(null);
    const [isPlaying, setIsPlaying] = useState(false);
    const [isMuted, setIsMuted] = useState(true);
    const [isHovered, setIsHovered] = useState(false);
    const [randomMovie, setRandomMovie] = useState(null);
    const [movies, setMovies] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchMovies = async () => {
            try {
                const token = '97f3f30c7512f8f507057e9c5752256a';
                const headers = {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                };
                const response = await fetch('http://localhost:4000/api/movies', {
                    headers: headers
                });
                const data = await response.json();
                setMovies(data);
                const randomIndex = Math.floor(Math.random() * data.length);
                setRandomMovie(data[randomIndex]);
                setLoading(false);
            } catch (error) {
                console.error('Error fetching movies:', error);
                setRandomMovie({
                    id: 1,
                    title: "Default Movie",
                    ageRating: "16+",
                    videoUrl: "/video_480.mp4"
                });
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

    const handleMoreInfo = () => {
        navigate('/info', { state: { movieId: randomMovie?.id } });
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

    const handleMouseEnter = () => setIsHovered(true);
    const handleMouseLeave = () => {
        setIsHovered(false);
        if (videoRef.current && isPlaying) {
            videoRef.current.pause();
            setIsPlaying(false);
        }
    };

    return (
        <div className="hero-section">
            <div
                className="hero-video-wrapper"
                onMouseEnter={handleMouseEnter}
                onMouseLeave={handleMouseLeave}
            >
                <VideoPlayer
                    ref={videoRef}
                    videoUrl={randomMovie?.videoUrl || "/video_480.mp4"}
                    onPlayPauseChange={setIsPlaying}
                    onMuteChange={setIsMuted}
                />

                <div className="movie-content">
                    <div className="movie-metadata">
                        <div className="netflix-title-container">
                            <h1 className="netflix-title">
                                {(randomMovie?.title || "Default Movie Title").split(' ').map((word, index, array) => (
                                    <span key={index} className="title-word">
                                        {word}
                                        {index < array.length - 1 ? ' ' : ''}
                                    </span>
                                ))}
                            </h1>
                        </div>
                    </div>
                    <div className="controls-container">
                        <button className="play-button" onClick={handlePlayPause}>
                            <span className="play-icon">▶</span>
                            Play
                        </button>
                        <button className="more-info-button" onClick={handleMoreInfo}>
                            <span className="info-icon">ℹ</span>
                            More Info
                        </button>
                    </div>
                </div>

                <div className="top-controls">
                    <button className="netflix-mute-button" onClick={handleMuteToggle}>
                        {isMuted ? '🔈' : '🔊'}
                    </button>
                    <span className="netflix-age-badge">
                        {randomMovie?.ageRating || "16+"}
                    </span>

                </div>
            </div>
        </div>
    );
};

export default PlayerHome;