import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import CharacterDisplay from './CharacterDisplay';
import AvatarSelector from './AvatarSelector';
import Input from '../../../components/common/Input/Input';
import Button from '../../../components/common/Button/Button';
import styles from './Register.module.css';

const AVATAR_OPTIONS = [
  '/path/to/avatar1.jpg',
  '/path/to/avatar2.jpg',
  '/path/to/avatar3.jpg',
  '/path/to/avatar4.jpg',
  '/path/to/avatar5.jpg',
  '/path/to/avatar6.jpg'
];

const SPECIAL_CHARS = '!@#$%^&*-_';

const Register = ({ normalImage, sunglassesImage }) => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    confirmPassword: '',
    displayName: '',
    selectedAvatar: null
  });
  
  const [validationState, setValidationState] = useState({
    isPasswordField: false,
    errors: {},
    isUsernameValid: false,
    touched: {
      username: false,
      password: false,
      confirmPassword: false,
      displayName: false
    }
  });

  // Validation rules
  const validateUsername = (username) => {
    if (!/^[a-zA-Z0-9]+$/.test(username)) {
      return "Username can only contain English letters and numbers";
    }
    if (username.length < 6 || username.length > 12) {
      return "Username must be 6-12 characters long";
    }
    return null;
  };

  const validatePassword = (password) => {
    const validChars = new RegExp(`^[a-zA-Z0-9${SPECIAL_CHARS}]+$`);
    if (!validChars.test(password)) {
      return "Password can only contain English letters, numbers, and special characters (!@#$%^&*-_)";
    }
    if (password.length < 8 || password.length > 20) {
      return "Password must be 8-20 characters long";
    }
    return null;
  };

  const validateConfirmPassword = (confirmPassword, password) => {
    if (confirmPassword !== password) {
      return "Passwords do not match";
    }
    return null;
  };

  const validateDisplayName = (displayName) => {
    if (!/^[a-zA-Z0-9]+$/.test(displayName)) {
      return "Display name can only contain English letters and numbers";
    }
    if (displayName.length < 6 || displayName.length > 12) {
      return "Display name must be 6-12 characters long";
    }
    return null;
  };

  // Validate all fields and check if form is valid
  const validateForm = () => {
    const errors = {
      username: validateUsername(formData.username),
      password: validatePassword(formData.password),
      confirmPassword: validateConfirmPassword(formData.confirmPassword, formData.password),
      displayName: validateDisplayName(formData.displayName)
    };

    setValidationState(prev => ({
      ...prev,
      errors
    }));

    return !Object.values(errors).some(error => error !== null);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));

    // Validate field on change
    let error = null;
    switch (name) {
      case 'username':
        error = validateUsername(value);
        break;
      case 'password':
        error = validatePassword(value);
        // Also validate confirm password if it exists
        if (formData.confirmPassword) {
          setValidationState(prev => ({
            ...prev,
            errors: {
              ...prev.errors,
              confirmPassword: validateConfirmPassword(formData.confirmPassword, value)
            }
          }));
        }
        break;
      case 'confirmPassword':
        error = validateConfirmPassword(value, formData.password);
        break;
      case 'displayName':
        error = validateDisplayName(value);
        break;
      default:
        break;
    }

    setValidationState(prev => ({
      ...prev,
      errors: {
        ...prev.errors,
        [name]: error
      },
      isUsernameValid: name === 'username' && !error
    }));
  };

  const handleFocus = (e) => {
    setValidationState(prev => ({
      ...prev,
      isPasswordField: e.target.type === 'password'
    }));
  };

  const handleBlur = (e) => {
    const { name } = e.target;
    setValidationState(prev => ({
      ...prev,
      isPasswordField: false,
      touched: {
        ...prev.touched,
        [name]: true
      }
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const isValid = validateForm();
    
    if (isValid) {
      console.log('Form is valid, submitting:', formData);
      // Add your submit logic here
    } else {
      console.log('Form has errors, please fix them');
    }
  };

  const handleAvatarSelect = (index) => {
    setFormData(prev => ({
      ...prev,
      selectedAvatar: index
    }));
  };

  // Check if the form is valid
  const isFormValid = !Object.values(validationState.errors).some(error => error !== null) &&
    formData.username && formData.password && formData.confirmPassword && formData.displayName;

  return (
    <div className={styles.container}>
      <div className={styles.formCard}>
        <div className={styles.formSection}>
          <h1 className={styles.title}>Create Account</h1>
          
          <form onSubmit={handleSubmit} className={styles.form}>
            <Input
              label="Username"
              type="text"
              name="username"
              value={formData.username}
              onChange={handleChange}
              onFocus={handleFocus}
              onBlur={handleBlur}
              error={validationState.touched.username ? validationState.errors.username : null}
              isValid={!validationState.errors.username && formData.username.length > 0}
              required
            />

            <Input
              label="Password"
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              onFocus={handleFocus}
              onBlur={handleBlur}
              error={validationState.errors.password}
              required
            />

            <Input
              label="Confirm Password"
              type="password"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
              onFocus={handleFocus}
              onBlur={handleBlur}
              error={validationState.errors.confirmPassword}
              required
            />

            <Input
              label="Display Name"
              type="text"
              name="displayName"
              value={formData.displayName}
              onChange={handleChange}
              onFocus={handleFocus}
              onBlur={handleBlur}
              error={validationState.errors.displayName}
              required
            />

            <AvatarSelector
              avatarOptions={AVATAR_OPTIONS}
              selectedAvatar={formData.selectedAvatar}
              onAvatarSelect={handleAvatarSelect}
            />

            <Button 
              type="submit" 
              variant="primary" 
              fullWidth
              disabled={!isFormValid}
            >
              Create Account
            </Button>

            <p className={styles.loginLink}>
              Already have an account? <Link to="/login">Sign in</Link>
            </p>
          </form>
        </div>

        <div className={styles.characterSection}>
          <CharacterDisplay 
            isPasswordField={validationState.isPasswordField}
            isValidating={true}
            normalImage={normalImage}
            sunglassesImage={sunglassesImage}
            username={formData.username}
            displayName={formData.displayName}
            isUsernameValid={!validationState.errors.username}
            hasValidDisplayName={!validationState.errors.displayName}
          />
        </div>
      </div>
    </div>
  );
};

export default Register;