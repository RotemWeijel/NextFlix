import React from 'react';
import { useTheme } from '../../../hooks/useTheme';
import styles from './LoadingSpinner.module.css';

export const LoadingSpinner = ({ size = 'medium' }) => {
  const { colors } = useTheme();
  
  return (
    <div 
      className={`${styles.spinner} ${styles[size]}`}
      style={{ '--spinner-color': colors.primary }}
    />
  );
};

export default LoadingSpinner;