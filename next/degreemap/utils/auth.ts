import {ACCESS_TOKEN} from "@/context/AuthContext";

export async function loginUser(email: string, password: string): Promise<string> {
    const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, password })
      });

    const data = await response.json();

    if(response.ok){
        localStorage.setItem(ACCESS_TOKEN, data.accessToken);
        return data.accesstoken;
    } else {
        throw new Error(data.message || 'Failed to login');
    }
}