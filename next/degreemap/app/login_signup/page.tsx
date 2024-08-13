"use client"

import AuthForm from "@/components/auth/AuthForm";
import NavBar from "@/components/nav/navbars";
import { useSession } from "next-auth/react";
import React, { useLayoutEffect } from "react";
import { redirect } from "next/navigation";

export default function LoginSignupPage() {
    const { data: session, status } = useSession();
    console.log(session);

    useLayoutEffect(() => {
        // If we're already authenticated, redirect to profile page
        if (session)
            redirect("/profile");
    }, [session]);

    // Don't render the screen if we're not unauthenticated
    // (status can be 'loading', 'authenticated', or 'unauthenticated')
    if (status != 'unauthenticated')
        return null;

    return <>
        <NavBar></NavBar>




        <AuthForm></AuthForm>
    </>
    
}