"use client"
import NavBar from "@/components/nav/navbars";
import React, { useEffect, useState } from "react";
import api from '@/utils/axiosInstance';
import { useSession } from 'next-auth/react';



const Page: React.FC = () => {
    const { data: session } = useSession();
    const [degreeMaps, setMaps] = useState<{ id: number, name: string }[]>([]);

    const getData = async () => {

        try {
            const res = await api.get('/degreeMaps');

            return res.data;
        } catch (error: any) {

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
                setMaps(data);
            }).catch(err => {
                console.error(err);
                setMaps([{ id: 0, name: "Error" }]);
            });
        } else {
            setMaps([{ id: 0, name: "Must be logged in" }]);
        }
    }, [session]);

    return (
        <>
            <NavBar></NavBar>
            <div>
                <h1>Protected page</h1>
                <h2>RESULT: </h2>
                {degreeMaps.map((degreemap) =>
                (
                    <h2 key={degreemap.id}>{degreemap.name}</h2>
                )
                )}
            </div>
        </>
    );
};

export default Page;