import React from 'react';
import Button from './components/common/Button/Button';
import Input from './components/common/Input/Input';
import LoadingSpinner from './components/common/LoadingSpinner/LoadingSpinner';
import Navbar from './components/common/Navbar/Navbar';
import Footer from './components/common/Footer/Footer';
import { useTheme } from './hooks/useTheme';

const TestComponents = () => {
  const { isDarkMode, toggleTheme } = useTheme();
  
  return (
    <div className="min-h-screen">
      <Navbar 
        isLoggedIn={true}
        userProfile={{
          avatar: "/default-avatar.png",
          name: "Test User"
        }}
        onLogout={() => console.log('logout clicked')}
      />
      
      <main className="p-8 space-y-8">
        <section className="space-y-4">
          <h2 className="text-2xl font-bold">Button Variants</h2>
          <div className="space-x-4">
            <Button variant="primary" size="small">Small Primary</Button>
            <Button variant="primary" size="medium">Medium Primary</Button>
            <Button variant="primary" size="large">Large Primary</Button>
          </div>
          <div className="space-x-4">
            <Button variant="secondary" size="small">Small Secondary</Button>
            <Button variant="secondary" size="medium">Medium Secondary</Button>
            <Button variant="secondary" size="large">Large Secondary</Button>
          </div>
          <div className="space-x-4">
            <Button variant="primary" disabled>Disabled Button</Button>
            <Button variant="primary" fullWidth>Full Width Button</Button>
          </div>
        </section>

        <section className="space-y-4 max-w-md">
          <h2 className="text-2xl font-bold">Input Variants</h2>
          <Input
            label="Text Input"
            type="text"
            placeholder="Enter some text"
          />
          <Input
            label="Email with Error"
            type="email"
            error="Please enter a valid email"
            placeholder="Enter your email"
          />
          <Input
            label="Password with Helper"
            type="password"
            helperText="Must be at least 8 characters"
            required
          />
          <Input
            label="Disabled Input"
            type="text"
            disabled
            value="Can't edit this"
          />
        </section>

        <section className="space-y-4">
          <h2 className="text-2xl font-bold">Loading Spinners</h2>
          <div className="space-x-8">
            <LoadingSpinner size="small" />
            <LoadingSpinner size="medium" />
            <LoadingSpinner size="large" />
          </div>
        </section>

        <section className="space-y-4">
          <h2 className="text-2xl font-bold">Theme Toggle</h2>
          <p>Current theme: {isDarkMode ? 'Dark' : 'Light'}</p>
          <Button onClick={toggleTheme}>Toggle Theme</Button>
        </section>
      </main>

      <Footer />
    </div>
  );
};

export default TestComponents;