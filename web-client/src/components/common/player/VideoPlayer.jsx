import React, { useState, useRef, useEffect, forwardRef } from 'react';
import './VideoPlayer.css';

const VideoPlayer = forwardRef(({ videoUrl, onPlayPauseChange, onMuteChange, onVideoClick }, ref) => {
  const [isPlaying, setIsPlaying] = useState(false);
  const [isMuted, setIsMuted] = useState(true);
  const videoRef = useRef(null);


  useEffect(() => {
    if (ref) {
      ref.current = {
        play: () => videoRef.current.play(),
        pause: () => videoRef.current.pause(),
        mute: () => {
          videoRef.current.muted = true;
          setIsMuted(true);
        },
        unmute: () => {
          videoRef.current.muted = false;
          setIsMuted(false);
        },
        setVolume: (value) => {
          videoRef.current.volume = value;
        },
        setCurrentTime: (time) => {
          videoRef.current.currentTime = time;
        },
        getIsPlaying: () => !videoRef.current.paused,
        getIsMuted: () => videoRef.current.muted

      };
    }
  }, [ref]);
  const handleClick = async () => {
    try {
      if (videoRef.current.paused) {
        await videoRef.current.play();
      } else {
        videoRef.current.pause();
      }
    } catch (err) {
      console.error("Error toggling play/pause:", err);
    }
  };

  return (
    <div className="video-container">
      <video
        ref={videoRef}
        className="video-element"
        src={videoUrl}
        muted={isMuted}
        onClick={handleClick}
      />
    </div>
  );
});


export default VideoPlayer;