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

    // Get a new access token

    const refreshCookie = getCookie('refreshToken');

    if (!refreshCookie) {
        signOut();
        return config;
    }

    const res = await fetch("http://localhost:8080/api/auth/refresh-token", {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${refreshCookie}`
        }
    });

    const { acccessToken: newAccessToken } = await res.json();

    config.headers['Authorization'] = `Bearer ${newAccessToken}`;
    return config;

}, (error) => {
    console.error("Error refreshing token");
    console.dir(error);
    signOut();
    return Promise.reject(error);
});

export default api;