import { useAuth } from "@/context/AuthContext";

const LogoutForm = () => {
    const { logout } = useAuth();
    
    const handleLogout = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        try {
            await logout();
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