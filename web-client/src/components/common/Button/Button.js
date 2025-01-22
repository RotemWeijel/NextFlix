import React from 'react';
import { useTheme } from '../../../hooks/useTheme';
import styles from './Button.module.css';

export const Button = ({
  variant = 'primary',
  size = 'medium',
  disabled = false,
  fullWidth = false,
  children,
  onClick,
  type = 'button',
  className = '',
  ...props
}) => {
  const { colors } = useTheme();

  const buttonClasses = [
    styles.button,
    styles[`button-${variant}`],
    styles[`button-${size}`],
    fullWidth && styles['button-fullWidth'],
    className
  ].filter(Boolean).join(' ');

  return (
    <button
      type={type}
      className={buttonClasses}
      disabled={disabled}
      onClick={onClick}
      style={{
        '--button-primary-color': colors.button.primary,
        '--button-secondary-color': colors.button.secondary,
        '--button-disabled-color': colors.button.disabled,
        '--button-text-color': colors.button.text
      }}
      {...props}
    >
      {children}
    </button>
  );
};

export default Button;