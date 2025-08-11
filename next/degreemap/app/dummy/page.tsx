// A page for testing protected routes
"use client"
import NavBar from '@/components/nav/navbars';
import api from '@/utils/axiosInstance';

import { useSession } from 'next-auth/react';
import React, { useEffect, useState } from 'react';

const Page: React.FC = () => {
    const { data: session } = useSession();
    const [msg, setMsg] = useState<string>('');

    const getData = async () => {

        try {
            const response = await api.get('/dummy/hello');

            return response.data.message;
        } catch (error: any) {
            
            // Runs when the server returns non-200 status code
            
            if (error.response?.status === 401) {
                console.error('401 MOMENT');
                console.dir(error); 
                return 'unauthorized, so you can\'t see it :(';
            }

            console.error('Error fetching dummy data: ', error);
            return 'An error occurred';
        }
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
                {/* How to access user's ID */}
                {session &&
                    <h2>Your ID is {session.user.id}</h2>
                }
            </div>
        </>
    );
};

export default Page;