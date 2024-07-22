import axios from "axios";
import { getSession, signOut } from "next-auth/react";
import { getCookie } from "cookies-next";


const api = axios.create({
    baseURL: 'http://localhost:8080/api',
})

const tokenRefreshInterceptor = api.interceptors.request.use(async (config) => {

    const session = await getSession();

    if (!session) {
        return config;
    }

    const acccessToken = session.accessToken;
    const acccessTokenExpires = new Date(session.accessTokenExpires).getTime(); // Unix timestamp

    if (Date.now() < acccessTokenExpires) {
        config.headers['Authorization'] = `Bearer ${acccessToken}`;
        return config;
    }

    throw new Error("Access token expired");

}, (error) => {
    console.error("An error occurred with the request interceptor:");
    console.dir(error);
    signOut();
    return Promise.reject(error);
});

export default api;