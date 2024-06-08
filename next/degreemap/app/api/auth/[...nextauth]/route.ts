// // If using NextAuth 4.x or later, the following imports are typically what you need:
// import NextAuth from 'next-auth';
// import CredentialsProvider from 'next-auth/providers/credentials';
// import type { NextAuthOptions } from 'next-auth';

// // This is the usual NextAuth configuration with TypeScript types
// const authOptions: NextAuthOptions = {
//   providers: [
//     CredentialsProvider({
//       name: 'Credentials',
//       credentials: {
//         email: { label: "Email", type: "email" },
//         password: { label: "Password", type: "password" }
//       },
//       authorize: async (credentials) => {
//         if (!credentials) return null;
//         const response = await fetch('http://localhost:8080/api/users/login', {
//           method: 'POST',
//           headers: { 'Content-Type': 'application/json' },
//           body: JSON.stringify({
//             email: credentials.email,
//             password: credentials.password
//           })
//         });
//         const user = await response.json();
//         if (response.ok && user) {
//           return user;
//         }
//         return null;
//       }
//     }),
//   ],
//   session: {
//     strategy: 'jwt',
//   },
//   callbacks: {
//     jwt: async ({ token, user, account }) => {
//       if (user) {
//         token.accessToken = user.jwt; // Assuming your user object includes the JWT
//       }
//       return token;
//     },
//     session: async ({ session, token }) => {
//       if (token) {
//         session.user.accessToken = token.accessToken; // Ensure this property is safely handled
//       }
//       return session;
//     }
//   },
//   secret: process.env.NEXTAUTH_SECRET,
//   jwt: {
//     secret: process.env.JWT_SECRET,
//     encryption: true,
//   },
// };

// export default NextAuth(authOptions);

