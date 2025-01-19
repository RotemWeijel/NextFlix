import React, { forwardRef } from 'react';
import { useTheme } from '../../../hooks/useTheme';
import styles from './Input.module.css';

export const Input = forwardRef(({
  type = 'text',
  label,
  error,
  fullWidth = false,
  className = '',
  disabled = false,
  required = false,
  helperText,
  id,
  ...props
}, ref) => {
  const { colors } = useTheme();
  const inputId = id || `input-${label?.toLowerCase().replace(/\s+/g, '-')}`;

  const inputClasses = [
    styles.input,
    error && styles.error,
    fullWidth && styles.fullWidth,
    disabled && styles.disabled,
    className
  ].filter(Boolean).join(' ');

  const containerClasses = [
    styles.container,
    fullWidth && styles.fullWidth
  ].filter(Boolean).join(' ');

  return (
    <div 
      className={containerClasses}
      style={{
        '--input-border-color': colors.border,
        '--input-text-color': colors.text.primary,
        '--input-background': colors.background.primary,
        '--input-error-color': colors.error,
        '--input-disabled-background': colors.background.secondary,
        '--input-label-color': colors.text.secondary
      }}
    >
      {label && (
        <label htmlFor={inputId} className={styles.label}>
          {label}
          {required && <span className={styles.required}>*</span>}
        </label>
      )}
      
      <div className={styles.inputWrapper}>
        <input
          ref={ref}
          id={inputId}
          type={type}
          disabled={disabled}
          className={inputClasses}
          required={required}
          {...props}
        />
      </div>

      {(error || helperText) && (
        <div className={`${styles.helperText} ${error ? styles.errorText : ''}`}>
          {error || helperText}
        </div>
      )}
    </div>
  );
});

Input.displayName = 'Input';

export default Input