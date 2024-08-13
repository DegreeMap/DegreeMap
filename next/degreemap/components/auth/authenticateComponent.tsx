"use client"

import { useSession } from "next-auth/react";
import { redirect } from "next/navigation";
import { useEffect } from "react";

export default function authenticateComponent(Component: any) {
    return function AuthedComponent(props: any) {
        const { data: session } = useSession();

        useEffect(() => {
            if (!session) {
               redirect('/login_signup');
            }
        }, []);

        if (!session)
            return null;

        return <Component {...props} />;
    };
}