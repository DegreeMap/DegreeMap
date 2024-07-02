import { createAccount } from "@/app/api/users/post";
import { useState } from "react";

const CreateAccountForm = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [status, setStatus] = useState('');

    const handleCreate = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        try {
            await createAccount(email, password);
            console.log('Create Account successful');
        } catch (error) {
            console.error('Create Account failed:', error);
            alert('Create Account failed: ' + error);
        }
    }

    return (
        <>
            <form onSubmit={handleCreate}>
                <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Email" />
                <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Password" />
                <button type="submit">Create</button>
                <p>{`${status}`}</p>
            </form>
        </>
    );
}

export default CreateAccountForm;