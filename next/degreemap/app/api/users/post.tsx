import { signIn } from "next-auth/react";

export const createAccount = async (email: string, password: string) => {
    const response = await fetch('http://localhost:8080/api/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            email, password
        })
      });

    if (!response.ok) {
        const data = await response.json();
        throw new Error(data.message || 'Failed to login');
    }

    // After registering the user, sign in the user
    signIn("credentials", {
        username: email,
        password: password,
    })
}