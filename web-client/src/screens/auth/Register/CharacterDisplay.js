import React, { useState, useEffect } from 'react';
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