import React, { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import VideoPlayer from '../../common/player/VideoPlayer';
import './PlayerHome.css';
import { getStoredToken, createAuthHeaders } from '../../../utils/auth';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:4000';

const PlayerHome = ({ }) => {
    const navigate = useNavigate();
    const videoRef = useRef(null);
    const [isPlaying, setIsPlaying] = useState(false);
    const [isMuted, setIsMuted] = useState(true);
    const [isHovered, setIsHovered] = useState(false);
    const [randomMovie, setRandomMovie] = useState(null);
    const [movies, setMovies] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(false)


    useEffect(() => {
        if (!getStoredToken()) {
            navigate('/login');
        }
    }, []);
    useEffect(() => {
        const fetchMovies = async () => {
            try {

                const headers = {
                    ...createAuthHeaders(),
                    'Content-Type': 'application/json'
                };
                const response = await fetch(`${API_BASE_URL}/api/movies`, {
                    headers: headers
                });
                const data = await response.json();
                const allMovies = data.reduce((acc, category) => {
                    return [...acc, ...category.movies];
                }, []);

                setMovies(allMovies);

                if (allMovies.length > 0) {
                    const randomIndex = Math.floor(Math.random() * allMovies.length);
                    const selectedMovie = allMovies[randomIndex];
                    const normalizedVideoUrl = selectedMovie.videoUrl ||
                        selectedMovie['videoUrl'] ||
                        selectedMovie[' videoUrl'] ||
                        selectedMovie['"videoUrl"'] ||
                        "/video_480.mp4";
                    setRandomMovie({
                        ...selectedMovie,
                        ageRating: selectedMovie.ageAllow + "+",
                        videoUrl: normalizedVideoUrl
                    });
                }
                setLoading(false);
            } catch (error) {
                setError(error)
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

    }, [setLoading, setError]);
    useEffect(() => {
        let playTimeout;

        if (isHovered && videoRef.current && !isPlaying) {
            playTimeout = setTimeout(async () => {
                try {
                    if (videoRef.current) {
                        await videoRef.current.play();
                        setIsPlaying(true);
                    }
                } catch (error) {
                    console.log('Video playback failed:', error);
                }
            }, 200);
        }

        return () => {
            clearTimeout(playTimeout);
            if (videoRef.current && isPlaying) {

                try {
                    videoRef.current.pause();
                } catch (error) {
                    console.log('Video pause failed:', error);
                }
                setIsPlaying(false);
            }
        };
    }, [isHovered, isPlaying]);
    /* divide the title to two */
    const formatTitle = (title) => {
        const words = title.split(' ');
        if (words.length > 5) {
            const firstHalfLength = Math.ceil(words.length / 2);
            const firstLine = words.slice(0, firstHalfLength).join(' ');
            const secondLine = words.slice(firstHalfLength).join(' ');
            return [firstLine, secondLine];
        }
        return [title];
    };

    const handlePlayPause = () => {
        const src = randomMovie.videoUrl
        navigate(`/Player/${randomMovie._id}?extraParam=${src}&movieId=${randomMovie._id}`);

    };

    const handleMoreInfo = () => {
        navigate(`/MovieDetails/${randomMovie._id}`);
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
                    videoUrl={randomMovie?.videoUrl}
                    onPlayPauseChange={setIsPlaying}
                    onMuteChange={setIsMuted}
                />

                <div className="movie-content">
                    <div className="movie-metadata">
                        <div className="netflix-title-container">
                            {randomMovie?.name && (
                                <h1 className="netflix-title">
                                    {randomMovie.name.split(' ').map((word, index, array) => (
                                        <span key={index} className="title-word">
                                            {word}
                                            {index < array.length - 1 ? ' ' : ''}
                                        </span>
                                    ))}
                                </h1>
                            )}
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