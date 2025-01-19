import React, { useState, useEffect } from 'react';
import LoadingSpinner from '../../../components/common/LoadingSpinner/LoadingSpinner';
import styles from './CharacterDisplay.module.css';

const CharacterDisplay = ({ 
  isPasswordField, 
  isValidating,
  normalImage, 
  sunglassesImage,
  username,
  displayName,
  isUsernameValid,
  hasValidDisplayName
}) => {
  // Determine which name to display
  const nameToDisplay = hasValidDisplayName ? displayName : (isUsernameValid ? username : null);

  return (
    <div className={styles.characterContainer}>
      <img
        src={isPasswordField ? sunglassesImage : normalImage}
        alt="Character"
        className={styles.characterImage}
      />
      
      <div className={styles.leftEyeSpinner}>
        <LoadingSpinner size="large" />
      </div>
      <div className={styles.rightEyeSpinner}>
        <LoadingSpinner size="large" />
      </div>

      {nameToDisplay && (
        <div className={styles.nameDisplay}>
          <span className={styles.welcomeText}>Welcome,</span>
          <span className={styles.name}>{nameToDisplay}</span>
        </div>
      )}
    </div>
  );
};

export default CharacterDisplay;