import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import Button from '../../../components/common/Button/Button';
import Input from '../../../components/common/Input/Input';
import styles from './Register.module.css';

const Register = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    confirmPassword: '',
    displayName: '',
    image: ''
  });
  const [errors, setErrors] = useState({});

  const handleSubmit = (e) => {
    e.preventDefault();
    // Add registration logic here
    console.log('Register attempt:', formData);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  return (
    <div className={styles.container}>
      <div className={styles.formCard}>
        <h1 className={styles.title}>Create Account</h1>
        <form onSubmit={handleSubmit} className={styles.form}>
          <Input
            label="Username"
            type="text"
            name="username"
            value={formData.username}
            onChange={handleChange}
            error={errors.username}
            required
          />
          <Input
            label="Password"
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            error={errors.password}
            required
          />
          <Input
            label="Confirm Password"
            type="password"
            name="confirmPassword"
            value={formData.confirmPassword}
            onChange={handleChange}
            error={errors.confirmPassword}
            required
          />
          <Input
            label="Display Name"
            type="text"
            name="displayName"
            value={formData.displayName}
            onChange={handleChange}
            error={errors.displayName}
            required
          />
          <Input
            label="Profile Image URL"
            type="text"
            name="image"
            value={formData.image}
            onChange={handleChange}
            error={errors.image}
          />
          <Button type="submit" variant="primary" fullWidth>
            Create Account
          </Button>
        </form>
        <p className={styles.loginLink}>
          Already have an account? <Link to="/login">Sign in</Link>
        </p>
      </div>
    </div>
  );
};

export default Register;