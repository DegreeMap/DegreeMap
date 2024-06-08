import NextAuth from 'next-auth';
import CredentialsProvider from 'next-auth/providers/credentials';
import { JWT, Session } from "next-auth";
import { JWTCallback, SessionCallback } from "next-auth/jwt"

const authOptions = {
  providers: [
    CredentialsProvider({
      name: 'Credentials',
      credentials: {
        email: { label: "Email", type: "email" },
        password: { label: "Password", type: "password" }
      },
      authorize: async (credentials) => {
        if (!credentials) return null;
        const response = await fetch('http://localhost:8080/api/users/login', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            email: credentials.email,
            password: credentials.password
          })
        });

        const user = await response.json();

        if (response.ok && user) {
          return user;
        }
        return null;
      }
    }),
  ],
  callbacks: {
    jwt: async ({ token, user, account }: JWTCallback) => {
      if (user) {
        token.accessToken = user.jwt;
      }
      return token as JWT;
    },
    session: async ({ session, token }: SessionCallback) => {
      if (token) {
        session.user.accessToken = token.accessToken;
      }
      return session as Session;
    }
  },
  secret: process.env.NEXTAUTH_SECRET,
  jwt: {
    secret: process.env.JWT_SECRET,
    encryption: true,
  },
};

export default NextAuth(authOptions);
