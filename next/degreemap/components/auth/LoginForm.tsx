import { signIn } from 'next-auth/react';
import React, { useState } from 'react';

const LoginForm = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    try {
      await signIn("credentials", {
        username: email,
        password: password,

        // If we want to redirect user to different page
        // redirect: true,
        // callbackUrl: "/profile"
      });
      console.log('Login successful');
    } catch (error) {
      console.error('Login failed:', error);
      alert('Login failed: ' + error);
    }
  };

  return (
    <form onSubmit={handleLogin}>
      <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Email" />
      <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Password" />
      <button type="submit">Login</button>
    </form>
  );
};

export default LoginForm;
