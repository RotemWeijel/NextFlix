import React, { useState, useRef, useEffect, forwardRef } from 'react';
import './VideoPlayer.css';

const VideoPlayer = forwardRef(({ videoUrl, onPlayPauseChange, onMuteChange }, ref) => {
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

  return (
    <div className="video-container">
      <video
        ref={videoRef}
        className="video-element"
        src={videoUrl}
        muted={isMuted}
      />
    </div>
  );
});

export default VideoPlayer;