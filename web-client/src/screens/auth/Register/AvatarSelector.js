import React from 'react';
import styles from './AvatarSelector.module.css';

const AvatarSelector = ({ 
  avatarOptions, 
  selectedAvatar, 
  onAvatarSelect 
}) => {
  return (
    <div className={styles.container}>
      <label className={styles.label}>
        Choose your Avatar:
      </label>
      <div className={styles.grid}>
        {avatarOptions.map((avatar, index) => (
          <button
            key={index}
            type="button"
            onClick={() => onAvatarSelect(index)}
            className={`${styles.avatarButton} ${
              selectedAvatar === index ? styles.selected : ''
            }`}
          >
            <img
              src={avatar}
              alt={`Avatar option ${index + 1}`}
              className={styles.avatarImage}
            />
          </button>
        ))}
      </div>
    </div>
  );
};

export default AvatarSelector;