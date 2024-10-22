import NextAuth from "next-auth/next";

declare module "next-auth" {
    interface Session {
        accessToken: string,
        expires: string,
        accessTokenExpires: string,
        user: {
            id: number,
            email: string,
        }
        error: string | null,
    }
}