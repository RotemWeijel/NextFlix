import React, { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import VideoPlayer from '../../components/common/player/VideoPlayer'
import './PlayerScreen.css';

const PlayerScreen = ({ movieId, initialQuality = "720", movieTitle = "" }) => {
    const navigate = useNavigate();
    const [isFullscreen, setIsFullscreen] = useState(false);
    const [selectedQuality, setSelectedQuality] = useState(initialQuality);
    const [showQualityMenu, setShowQualityMenu] = useState(false);
    const [showSpeedMenu, setShowSpeedMenu] = useState(false);
    const [playbackSpeed, setPlaybackSpeed] = useState(1);
    const [isPlaying, setIsPlaying] = useState(true);
    const [isMuted, setIsMuted] = useState(true);
    const [volume, setVolume] = useState(100);
    const containerRef = useRef(null);
    const videoRef = useRef(null);
    const [currentTime, setCurrentTime] = useState(0);
    const [duration, setDuration] = useState(0);

    const qualityOptions = [
        { value: "1080", label: "1080p" },
        { value: "720", label: "720p" },
        { value: "480", label: "480p" },
        { value: "360", label: "360p" }
    ];

    const speedOptions = [
        { value: 0.25, label: "0.25x" },
        { value: 0.5, label: "0.5x" },
        { value: 0.75, label: "0.75x" },
        { value: 1, label: "Normal" },
        { value: 1.25, label: "1.25x" },
        { value: 1.5, label: "1.5x" },
        { value: 1.75, label: "1.75x" },
        { value: 2, label: "2x" }
    ];

    useEffect(() => {
        const videoElement = containerRef.current?.querySelector('video');
        if (videoElement) {
            const handleTimeUpdate = () => {
                setCurrentTime(videoElement.currentTime);
            };

            const handleLoadedMetadata = () => {
                setDuration(videoElement.duration);
            };

            videoElement.addEventListener('timeupdate', handleTimeUpdate);
            videoElement.addEventListener('loadedmetadata', handleLoadedMetadata);

            return () => {
                videoElement.removeEventListener('timeupdate', handleTimeUpdate);
                videoElement.removeEventListener('loadedmetadata', handleLoadedMetadata);
            };
        }
    }, []);

    useEffect(() => {
        const handleFullscreenChange = () => {
            setIsFullscreen(!!document.fullscreenElement);
        };


        document.addEventListener('fullscreenchange', handleFullscreenChange);
        document.addEventListener('webkitfullscreenchange', handleFullscreenChange);
        document.addEventListener('mozfullscreenchange', handleFullscreenChange);
        document.addEventListener('MSFullscreenChange', handleFullscreenChange);

        return () => {
            document.removeEventListener('fullscreenchange', handleFullscreenChange);
            document.removeEventListener('webkitfullscreenchange', handleFullscreenChange);
            document.removeEventListener('mozfullscreenchange', handleFullscreenChange);
            document.removeEventListener('MSFullscreenChange', handleFullscreenChange);
        };
    }, []);

    useEffect(() => {
        const initializeVideo = async () => {
            if (videoRef.current) {
                try {
                    await videoRef.current.play();
                    setIsPlaying(true);
                } catch (err) {
                    console.error("Auto-play failed:", err);
                    setIsPlaying(false);
                }
            }
        };

        initializeVideo();
    }, []);

    const handlePlayPause = () => {
        if (videoRef.current) {
            if (isPlaying) {
                videoRef.current.pause();
            } else {
                videoRef.current.play();
            }
            setIsPlaying(!isPlaying);
        }
    };

    const handleProgressBarClick = (e) => {
        const progressBar = e.currentTarget;
        const rect = progressBar.getBoundingClientRect();
        const clickPosition = (e.clientX - rect.left) / rect.width;

        if (videoRef.current) {
            const videoElement = containerRef.current.querySelector('video');
            if (videoElement && duration) {
                const newTime = Math.max(0, Math.min(clickPosition * duration, duration));
                videoElement.currentTime = newTime;
                setCurrentTime(newTime);
            }
        }
    };

    const handleVolumeChange = (e) => {
        const newVolume = parseInt(e.target.value) / 100;
        setVolume(e.target.value);
        if (videoRef.current) {
            videoRef.current.setVolume(newVolume);
        }
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

    const handleFullscreenToggle = async () => {
        try {
            if (!isFullscreen) {
                const element = containerRef.current;
                if (element.requestFullscreen) {
                    await element.requestFullscreen();
                } else if (element.webkitRequestFullscreen) {
                    await element.webkitRequestFullscreen();
                } else if (element.mozRequestFullScreen) {
                    await element.mozRequestFullScreen();
                } else if (element.msRequestFullscreen) {
                    await element.msRequestFullscreen();
                }
            } else {
                if (document.exitFullscreen) {
                    await document.exitFullscreen();
                } else if (document.webkitExitFullscreen) {
                    await document.webkitExitFullscreen();
                } else if (document.mozCancelFullScreen) {
                    await document.mozCancelFullScreen();
                } else if (document.msExitFullscreen) {
                    await document.msExitFullscreen();
                }
            }
        } catch (error) {
            console.error('Fullscreen error:', error);
        }
    };

    const handleQualityChange = (quality) => {
        setSelectedQuality(quality);
        setShowQualityMenu(false);
    };

    const handleSpeedChange = (speed) => {
        setPlaybackSpeed(speed);
        setShowSpeedMenu(false);
        if (videoRef.current) {
            const videoElement = containerRef.current.querySelector('video');
            if (videoElement) {
                videoElement.playbackRate = speed;

                const currentTime = videoElement.currentTime;

                videoElement.playbackRate = speed;

                videoElement.currentTime = currentTime;
            }
        }
    };

    const handleHomeClick = () => {
        navigate('/');
    };


    return (
        <div className="player-screen" ref={containerRef}>
            <button className="home-button" onClick={handleHomeClick}>
                Home →
            </button>

            <VideoPlayer
                videoUrl={`/video_${selectedQuality}.mp4`}
                ref={videoRef}

            />

            <div className="player-controls">
                <div
                    className="progress-bar-container"
                    onClick={handleProgressBarClick}
                >
                    <div className="progress-bar">
                        <div
                            className="progress-bar-filled"
                            style={{ width: `${(currentTime / duration) * 100}%` }}
                        />
                    </div>
                </div>

                <div className="controls-row">
                    <div className="controls-left">
                        <button className="control-button" onClick={handlePlayPause}>
                            {isPlaying ? '⏸' : '▶'}
                        </button>

                        <div className="volume-control">
                            <button className="control-button" onClick={handleMuteToggle}>
                                {isMuted ? '🔇' : '🔊'}
                            </button>
                            <input
                                type="range"
                                min="0"
                                max="100"
                                value={volume}
                                onChange={handleVolumeChange}
                                className="volume-slider"
                            />
                        </div>
                    </div>

                    <div className="controls-right">
                        <div className="speed-control">
                            <button
                                className="control-button"
                                onClick={() => {
                                    setShowSpeedMenu(!showSpeedMenu);
                                    setShowQualityMenu(false);
                                }}
                            >
                                {playbackSpeed}x
                            </button>

                            {showSpeedMenu && (
                                <div className="control-menu">
                                    {speedOptions.map((option) => (
                                        <button
                                            key={option.value}
                                            className={`menu-option ${playbackSpeed === option.value ? 'active' : ''}`}
                                            onClick={() => handleSpeedChange(option.value)}
                                        >
                                            {option.label}
                                        </button>
                                    ))}
                                </div>
                            )}
                        </div>

                        <div className="quality-control">
                            <button
                                className="control-button"
                                onClick={() => {
                                    setShowQualityMenu(!showQualityMenu);
                                    setShowSpeedMenu(false);
                                }}
                            >
                                {selectedQuality}p
                            </button>

                            {showQualityMenu && (
                                <div className="control-menu">
                                    {qualityOptions.map((option) => (
                                        <button
                                            key={option.value}
                                            className={`menu-option ${selectedQuality === option.value ? 'active' : ''}`}
                                            onClick={() => handleQualityChange(option.value)}
                                        >
                                            {option.label}
                                        </button>
                                    ))}
                                </div>
                            )}
                        </div>

                        <button
                            className="control-button"
                            onClick={handleFullscreenToggle}
                            title={isFullscreen ? "Exit Fullscreen" : "Enter Fullscreen"}
                        >
                            {isFullscreen ? '⊠' : '⊞'}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default PlayerScreen;