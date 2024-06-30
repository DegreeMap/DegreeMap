"use client"

import { loginUser } from '@/utils/auth';
import React, { createContext, useContext, useState, useEffect } from 'react';

interface Props {
    children: React.ReactNode;
  }

export const ACCESS_TOKEN = 'access_token'

interface AuthContextType {
  token: string | null;
  isAuthenticated: boolean;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<Props> = ({ children }) => {
  const [token, setToken] = useState<string | null>(null);

  useEffect(() => {
    const storedToken = localStorage.getItem(ACCESS_TOKEN);
    if (storedToken) {
      setToken(storedToken);
    }
  }, []);

  const isAuthenticated = Boolean(token);

  const login = async (email: string, password: string) => {
    await loginUser(email, password);
    const newToken = localStorage.getItem(ACCESS_TOKEN);
    setToken(newToken);
  };

  const logout = () => {
    localStorage.removeItem(ACCESS_TOKEN);
    setToken(null);
  };

  return (
    <AuthContext.Provider value={{ token, isAuthenticated, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};