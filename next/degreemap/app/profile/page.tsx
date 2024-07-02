"use client"

import CreateAccountForm from "@/components/auth/CreateAccountForm";
import LoginForm from "@/components/auth/LoginForm";
import LogoutForm from "@/components/auth/LogoutForm";
import NavBar from "@/components/nav/navbars";
// import { useAuth } from "@/context/AuthContext";
import { signIn, signOut, useSession } from "next-auth/react";
import React from "react";

export default function ProfilePage() {    
    const { data: session } = useSession();

    // const { isAuthenticated } = useAuth();
    
    if(session?.user){
        return (
            <>
                <NavBar></NavBar>
                <p>profile page!</p>
                <br></br>
                <br></br>
                <p>you are logged in, congrats!</p>
                {/* <LogoutForm></LogoutForm> */}
                <button onClick={() => signOut()}>SIGN OUT HERE</button>
            </>
        )
    }
    else {
        return (
            <>
                <NavBar></NavBar>
                <p>profile page!</p>
                <br></br>
                <br></br>
                <p>you are logged out. boo!</p>
                {/* <LoginForm></LoginForm> */}
                <button onClick={() => signIn()}>LOG IN HERE</button>
                <br></br>
                <p>don't have an account? make one here</p>
                <CreateAccountForm></CreateAccountForm>
            </>
        );
    }
}