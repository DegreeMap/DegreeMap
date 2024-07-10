// A page for testing protected routes
"use client"
import NavBar from '@/components/nav/navbars';

import { useSession } from 'next-auth/react';
import React, { useEffect, useState } from 'react';

const Page: React.FC = () => {
    const { data: session } = useSession();
    const [msg, setMsg] = useState<string>('');

    const getData = async () => {
        const res = await fetch('http://localhost:8080/api/dummy/hello', {
            method: 'GET',
            headers: {
                "Authorization": "Bearer " + session?.user.accessToken,
            }
        });

        if (!res.ok) {
            return res.status === 401 ? "Unauthorized, so you can't see it :(" : 'Backend error: ' + res.status;
        }

        const data = await res.json();
        return data.message;
    }

    useEffect(() => {
        if (session) {
            getData().then(data => {
                setMsg(data);
            }).catch(err => {
                console.error(err);
                setMsg('An error occurred');
            });
        } else {
            setMsg('You need to be logged in to see this page');
        }
    }, [session]);

    return (
        <>
            <NavBar></NavBar>
            <div>
                <h1>Protected page</h1>
                <h2>MESSAGE: {msg}</h2>
            </div>
        </>
    );
};

export default Page;