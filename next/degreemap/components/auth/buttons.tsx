"use client"

import { signIn, signOut, useSession } from "next-auth/react";

export default function AuthButton() {
    const {data: session} = useSession();
    
    if(session){
        return (
            <>
                <br></br>
                <br></br>
                <br></br>
                <br></br>
                <p>signed in!</p>
                <button onClick={() => signOut()}>click here to sign out</button>
            </>
        );
    }
    else{
        return (
            <>
                <br></br>
                <br></br>
                <br></br>
                <br></br>
                <p>not signed in</p>
                <button onClick={() => signIn()}> click here to sign in</button>
            </>
        );
    }
};

// This is used for example purposes.