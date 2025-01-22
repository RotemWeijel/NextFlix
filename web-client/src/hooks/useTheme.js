import React, { createContext, useContext, useState, useEffect } from 'react';
import { darkColors, lightColors } from '../styles/theme/colors';

export const ThemeContext = createContext({
  isDark: false,
  setIsDark: () => { },
  colors: lightColors,
  toggleTheme: () => { },
});

export const useTheme = () => {
  const context = useContext(ThemeContext);
  if (!context) {
    throw new Error('useTheme must be used within a ThemeProvider');
  }
  return context;
};

export const ThemeProvider = ({ children }) => {
  const savedTheme = localStorage.getItem('theme');
  const [isDark, setIsDark] = useState(savedTheme === 'dark');
  const [colors, setColors] = useState(isDark ? darkColors : lightColors);

  const toggleTheme = () => {
    setIsDark(prev => !prev);
  };

  useEffect(() => {
    const currentColors = isDark ? darkColors : lightColors;
    setColors(currentColors);
    localStorage.setItem('theme', isDark ? 'dark' : 'light');

    document.documentElement.style.setProperty('--background-primary', currentColors.background.primary);
    document.documentElement.style.setProperty('--background-secondary', currentColors.background.secondary);
    document.documentElement.style.setProperty('--text-primary', currentColors.text.primary);
    document.documentElement.style.setProperty('--text-secondary', currentColors.text.secondary);
    document.documentElement.style.setProperty('--primary', currentColors.primary);
    document.documentElement.style.setProperty('--secondary', currentColors.secondary);
    document.documentElement.style.setProperty('--button-primary', currentColors.button.primary);
    document.documentElement.style.setProperty('--button-secondary', currentColors.button.secondary);
    document.documentElement.style.setProperty('--button-disabled', currentColors.button.disabled);
    document.documentElement.style.setProperty('--button-text', currentColors.button.text);
    document.documentElement.style.setProperty('--border', currentColors.border);
    document.documentElement.style.setProperty('--error', currentColors.error);

    document.body.style.backgroundColor = currentColors.background.primary;
    document.body.style.color = currentColors.text.primary;
  }, [isDark]);

  const value = {
    isDark,
    setIsDark,
    colors,
    toggleTheme,
  };

  return (
    <ThemeContext.Provider value={value}>
      {children}
    </ThemeContext.Provider>
  );
};

export default ThemeProvider;
