import NextAuth, { Session } from "next-auth"
import CredentialsProvider from "next-auth/providers/credentials";

interface Credentials {
  email: string;
  password: string;
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
        
        const res = await fetch("http://localhost:8080/api/users", {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            email: credentials?.email,
            password: credentials?.password
          })
        });

        const user= await res.json();

        if (res.ok && user) {
          return user;
        }
        return null;
      }
    }),
  ],
  // session: {
  //   strategy: "jwt"
  // },
  // callbacks: {
  //   jwt: async ({ token, user }) => {
  //     if (user) {
  //       token.id = user.id;
  //       token.email = user.email;
  //     }
  //     return token;
  //   },
  //   session: async ({ session, token }) => {
  //     session.user.id = token.id as string;
  //     session.user.email = token.email as string;
  //     return session;
  //   }
  // }
};

export const handler = NextAuth(authOptions)
export { handler as GET, handler as POST };