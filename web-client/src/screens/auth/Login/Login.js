import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import Button from '../../../components/common/Button/Button';
import Input from '../../../components/common/Input/Input';
import styles from './Login.module.css';

const Login = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: '',
    password: ''
  });
  const [errors, setErrors] = useState({});

  const handleSubmit = (e) => {
    e.preventDefault();
    // Add login logic here
    console.log('Login attempt:', formData);
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
        <h1 className={styles.title}>Sign In</h1>
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
          <Button type="submit" variant="primary" fullWidth>
            Sign In
          </Button>
        </form>
        <p className={styles.registerLink}>
          New to Netflix? <Link to="/register">Sign up now</Link>
        </p>
      </div>
    </div>
  );
};

export default Login;