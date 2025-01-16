import React from 'react';
import { useTheme } from '../../../hooks/useTheme';
import styles from './Footer.module.css';

export const Footer = () => {
  const { colors } = useTheme();

  return (
    <footer 
      className={styles.footer}
      style={{
        '--footer-bg': colors.background.secondary,
        '--footer-text': colors.text.secondary
      }}
    >
      <div className={styles.footerContent}>
        <div className={styles.footerSection}>
          <h4>Questions? Contact us.</h4>
          <a href="/help">FAQ</a>
          <a href="/investor-relations">Investor Relations</a>
          <a href="/privacy">Privacy</a>
          <a href="/speed-test">Speed Test</a>
        </div>

        <div className={styles.footerSection}>
          <a href="/help-center">Help Center</a>
          <a href="/jobs">Jobs</a>
          <a href="/cookie-preferences">Cookie Preferences</a>
          <a href="/legal-notices">Legal Notices</a>
        </div>

        <div className={styles.footerSection}>
          <a href="/account">Account</a>
          <a href="/ways-to-watch">Ways to Watch</a>
          <a href="/corporate-information">Corporate Information</a>
          <a href="/only-on-netflix">Only on Netflix</a>
        </div>

        <div className={styles.footerSection}>
          <a href="/media-center">Media Center</a>
          <a href="/terms">Terms of Use</a>
          <a href="/contact-us">Contact Us</a>
        </div>
      </div>

      <div className={styles.bottomSection}>
        <div className={styles.social}>
          <a href="https://facebook.com/netflix" target="_blank" rel="noopener noreferrer">Facebook</a>
          <a href="https://twitter.com/netflix" target="_blank" rel="noopener noreferrer">Twitter</a>
          <a href="https://instagram.com/netflix" target="_blank" rel="noopener noreferrer">Instagram</a>
          <a href="https://youtube.com/netflix" target="_blank" rel="noopener noreferrer">YouTube</a>
        </div>
        
        <p className={styles.copyright}>
          © {new Date().getFullYear()} Netflix Clone Project. All rights reserved.
        </p>
      </div>
    </footer>
  );
};