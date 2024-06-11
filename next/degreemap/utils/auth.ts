export async function loginUser(email: string, password: string): Promise<string> {
    const response = await fetch('http://localhost:8080/api/users/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, password })
      });

    const data = await response.json();

    if(response.ok){
        localStorage.setItem('token', data.jwtToken);
        return data.jwtToken;  
    } else {
        throw new Error(data.message || 'Failed to login');
    }
}