import axios from "axios";
import NextAuth from "next-auth";
import Credentials from "next-auth/providers/credentials";
import { cookies } from "next/headers";

const handler = NextAuth({
    providers: [
        Credentials({
            // The name to display on the sign in form (e.g. 'Sign in with...')
            name: 'Credentials',
            // The credentials is used to generate a suitable form on the sign in page.
            // You can specify whatever fields you are expecting to be submitted.
            // e.g. domain, username, password, 2FA token, etc.
            credentials: {
                username: { label: "Username", type: "text", placeholder: "jsmith" },
                password: {  label: "Password", type: "password" }
            },
            async authorize(credentials, req) {
                // You need to provide your own logic here that takes the credentials
                // submitted and returns either a object representing a user or value
                // that is false/null if the credentials are invalid.
                // e.g. return { id: 1, name: 'J Smith', email: 'jsmith@example.com' }
                // You can also use the `req` object to obtain additional parameters
                // (i.e., the request IP address)
                const res = await fetch("http://localhost:8080/api/auth/login", {
                    method: 'POST',
                    body: new URLSearchParams([
                        ["email", credentials?.username ?? ""],
                        ["password", credentials?.password ?? ""]
                    ]).toString(),
                    headers: { "Content-Type": "application/x-www-form-urlencoded" }
                });

                const refreshCookie = res.headers.get('Set-Cookie')!!;
                const refreshToken = refreshCookie.split('=')[1].split(';')[0];

                cookies().set('refreshToken', refreshToken);

                const user = await res.json();

                // If no error and we have user data, return it
                if (res.ok && user) {
                    return user
                }
                // Return null if user data could not be retrieved
                return null
            }
        })
    ],
    secret: process.env.NEXTAUTH_SECRET,
    callbacks: {
        async jwt({ token, user }) {
            // When user is logged in for the first time

            if (user) {
                token.accessToken = user.accessToken;
                token.accessTokenExpires = new Date(user.expiresAt).getTime();
            }

            // If token has not expired, return it
            if (token?.accessTokenExpires && Date.now() < token.accessTokenExpires) {
                return token;
            }

            // Access token has expired, try to update it
            return refreshAccessToken(token);
        },
        async session({ session, token, user }) {

            // Check if token is expired
            const accessTokenExpires = new Date(token.accessTokenExpires as string).getTime();

            if (accessTokenExpires && Date.now() < accessTokenExpires) {
                session.accessToken = token.accessToken as any;
                session.accessTokenExpires = token.accessTokenExpires as any;
                return session;
            }

            // Token has expired, refresh it

            token = await refreshAccessToken(token);

            session.accessToken = token.accessToken as any;
            session.accessTokenExpires = token.accessTokenExpires as any;
            return session;
        },
    }
});

async function refreshAccessToken(token: any) {
    try {
        const refreshCookie = cookies().get('refreshToken');

        if (!refreshCookie) {
            return {
                ...token,
                error: 'NoRefreshTokenError'
            }
        }

        const res = await axios.post("http://localhost:8080/api/auth/refresh-token", {}, {
            headers: {
                'Authorization': `Bearer ${refreshCookie.value}`
            }
        });
        const data = res.data;

        return {
            ...token,
            accessToken: data.accessToken,
            accessTokenExpires: data.expiresAt,
        }

    } catch (error) {
        console.error("Error refreshing token from route.ts");
        console.dir(error);
        return {
            ...token,
            error: 'RefreshAccessTokenError'
        }
    
    }
}

export { handler as GET, handler as POST };