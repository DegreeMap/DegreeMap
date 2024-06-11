export const createAccount = async (email: string, password: string) => {
    const response = await fetch('http://localhost:8080/api/users', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, password })
      });

    const data = await response.json();

    if(response.ok){
        return data;
    } else {
        throw new Error(data.message || 'Failed to login');
    }
}