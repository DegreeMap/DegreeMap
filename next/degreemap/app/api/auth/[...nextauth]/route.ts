import NextAuth, { Session } from "next-auth"
import { JWT } from "next-auth/jwt";
import CredentialsProvider from "next-auth/providers/credentials";
import GithubProvider from "next-auth/providers/github"
import GoogleProvider from "next-auth/providers/google"

interface Credentials {
  email: string;
  password: string;
}

interface User {
  id: string;
  name?: string;
  email?: string;
}

export const authOptions = {
  providers: [
    CredentialsProvider({
      name: 'Credentials',
      credentials: {
        email: { label: "Email", type: "email", placeholder: "you@example.com" },
        password: { label: "Password", type: "password", placeholder: "Password" }
      },
      authorize: async (credentials) => {
        if (!credentials) return null;
        
        const res = await fetch("http://localhost:8080/api/login", {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            email: credentials.email,
            password: credentials.password
          })
        });

        const user: User | null = await res.json();

        if (res.ok && user) {
          return user;
        }
        return null;
      }
    }),
  ],
}
export const handler = NextAuth(authOptions)
export { handler as GET, handler as POST };