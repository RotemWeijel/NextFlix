import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import VideoPlayer from '../../common/player/VideoPlayer';
import './VideoPlayerDetails.css';
import { getStoredToken, createAuthHeaders } from '../../../utils/auth';
const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:4000';

const VideoPlayerDetails = ({ movieId }) => {
    const [isPlaying, setIsPlaying] = useState(false);
    const [isMuted, setIsMuted] = useState(true);
    const videoRef = useRef();
    const timeoutRef = useRef(null);
    const navigate = useNavigate();
    const [movie, setMovie] = useState([])

    useEffect(() => {
        const fetchMovie = async () => {
            try {
                const actualId = typeof movieId === 'object' ? movieId.movie : movieId;
                const headers = {
                    ...createAuthHeaders(),
                    'Content-Type': 'application/json'
                };
                const url = `${API_BASE_URL}/api/movies/${actualId}`;
                const res = await fetch(url, {
                    headers: headers
                });

                if (!res.ok) {
                    throw new Error(`HTTP error! status: ${res.status}`);
                }
                const data = await res.json();
                setMovie(data);
            } catch (error) {
                console.error('Error fetching movie:', error);
            }
        };

        if (movieId) {
            fetchMovie();
        }
    }, [movieId]);

    useEffect(() => {
        return () => {
            if (timeoutRef.current) {
                clearTimeout(timeoutRef.current);
            }
        };
    }, []);

    const handleMouseEnter = () => {
        if (timeoutRef.current) {
            clearTimeout(timeoutRef.current);
        }
        timeoutRef.current = setTimeout(() => {
            if (videoRef.current) {
                videoRef.current.play().catch(err => {
                    console.error("Play failed:", err);
                });
                setIsPlaying(true);
            }
        }, 300);
    };

    const handleMouseLeave = () => {
        if (timeoutRef.current) {
            clearTimeout(timeoutRef.current);
        }
        timeoutRef.current = setTimeout(() => {
            if (videoRef.current) {
                videoRef.current.pause();
                setIsPlaying(false);
            }
        }, 300);
    };


    const handleMuteToggle = () => {
        if (videoRef.current) {
            videoRef.current.muted = !isMuted;
            setIsMuted(!isMuted);
        }
    };

    const handleClose = () => {
        if (videoRef.current) {
            videoRef.current.pause();
        }
        navigate('/movies/browse');
    };
    const handleclickPlay = () => {
        const src = movie.videoUrl
        navigate(`/movies/${movie._id}/play?extraParam=${src}&movieId=${movie._id}`);
    }
    const handleEdit = () => {
        navigate(`/admin/movies/${movie._id}/edit`);
    }

    const isAdmin = () => {
        const userStr = localStorage.getItem('user');
        try {
            const user = userStr ? JSON.parse(userStr) : null;
            return user && user.isAdmin;
        } catch (error) {
            console.error('Error parsing stored user:', error);
            return false;
        }
    };

    return (
        <div className="movie-player-container">
            <div className="title-container">
                <h1 className="video-title">{movie.name}</h1>
            </div>
            <div
                className="video-wrapper"
                onMouseEnter={handleMouseEnter}
                onMouseLeave={handleMouseLeave}
            >
                <VideoPlayer
                    ref={videoRef}
                    videoUrl={movie.videoUrl}
                />
                <div className="controls-layer">
                    <div className="top-controls">
                        <button className="close-button" onClick={handleClose}>
                            ✕
                        </button>
                    </div>

                    <div className="bottom-controls">
                        <div className="left-controls">
                            <button onClick={handleclickPlay} className="play-button" >
                                {'▶ Play'}
                            </button>
                            <button className="circle-button" title="Add to My List">
                                +
                            </button>
                            <button className="circle-button" title="Like">
                                👍
                            </button>
                            {isAdmin() && (
                                <button
                                    className="edit-button"
                                    title="Edit Movie"
                                    onClick={handleEdit}
                                >
                                    ✎
                                </button>
                            )}
                        </div>

                        <button
                            className="volume-button"
                            onClick={handleMuteToggle}
                            title={isMuted ? 'Unmute' : 'Mute'}
                        >
                            {isMuted ? '🔇' : '🔊'}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default VideoPlayerDetails;
