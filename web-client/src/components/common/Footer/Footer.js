import React from 'react';
import { Link } from 'react-router-dom';
import { useTheme } from '../../../hooks/useTheme';
import styles from './Footer.module.css';

export const Footer = () => {
  const { colors } = useTheme();

  return (
    <div className={styles.footerWrapper}>
      <footer 
        className={styles.footer}
        style={{
          '--footer-bg': colors.background.primary,
          '--footer-text': colors.text.secondary,
          '--footer-border': colors.border
        }}
      >
        <div className={styles.footerContent}>
          <div className={styles.footerLinks}>
            <Link to="/">Home</Link>
          </div>
          
          <p className={styles.disclaimer}>
            This project was created as part of Advanced Programming course at Bar Ilan University (BIU).
          </p>

          <p className={styles.copyright}>
            © {new Date().getFullYear()} NextFlix
          </p>
        </div>
      </footer>
    </div>
  );
};

export default Footer;