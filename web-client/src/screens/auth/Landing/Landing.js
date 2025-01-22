import React from 'react';
import { useNavigate } from 'react-router-dom';
import Button from '../../../components/common/Button/Button';
import styles from './Landing.module.css';

const Landing = () => {
  const navigate = useNavigate();

  const features = [
    {
      title: "Instant Streaming",
      description: "Enjoy instant streaming of your favorite content directly in your browser or app. No downloads needed.",
      imageUrl: "/images/Landing/features/streaming.png"
    },
    {
      title: "Watch Everywhere",
      description: "Access your favorite content seamlessly on both Web and Android devices. Switch between platforms effortlessly.",
      imageUrl: "/images/Landing/features/devices.png"
    },
    {
      title: "Smart Recommendations",
      description: "Get personalized content suggestions based on your watching habits and preferences.",
      imageUrl: '/images/Landing/features/recommendations.png'
    }
  ];

  return (
    <div className={styles.container}>
      <div className={styles.content}>
        <div className={styles.heroSection}>
          <h1 className={styles.mainTitle}>NEXTFLIX</h1>
          <h2 className={styles.title}>Unlimited movies, TV shows, and more</h2>
          <p className={styles.subtitle}>Watch anywhere. Completely free.</p>
        </div>

        <div className={styles.features}>
          {features.map((feature, index) => (
            <div key={index} className={styles.featureCard}>
              <div className={styles.featureImageContainer}>
                <img 
                  src={feature.imageUrl} 
                  alt={feature.title}
                  className={styles.featureImage}
                />
              </div>
              <h3 className={styles.featureTitle}>{feature.title}</h3>
              <p className={styles.featureDescription}>{feature.description}</p>
            </div>
          ))}
        </div>

        <div className={styles.buttons}>
          <Button 
            variant="primary" 
            size="large" 
            onClick={() => navigate('/register')}
          >
            Get Started
          </Button>
          <Button 
            variant="secondary" 
            size="large" 
            onClick={() => navigate('/login')}
          >
            Sign In
          </Button>
        </div>
      </div>
    </div>
  );
};

export default Landing;