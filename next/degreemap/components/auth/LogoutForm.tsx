import { signOut } from "next-auth/react";

const LogoutForm = () => {
    
    const handleLogout = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        try {
            await signOut();
            console.log('Logout successful');
        } catch (error) {
            console.error('Logout failed:', error);
            alert('Logout failed: ' + error);
        }
    };

    return (
        <>
            <form onSubmit={handleLogout}>
                <button type="submit">Logout</button>
            </form>
        </>
    );
}

export default LogoutForm;