import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext'; // Adjust the path as necessary

const LoginForm = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const { login } = useAuth();

  const handleLogin = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    try {
      await login(email, password);
      console.log('Login successful');
    } catch (error) {
      console.error('Login failed:', error);
      alert('Login failed: ' + error);
    }
  };

  console.log("I'm happning!");

  return (
    <form onSubmit={handleLogin}>
      <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Email" />
      <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Password" />
      <button type="submit">Login</button>
    </form>
  );
};

export default LoginForm;
