"use client"

// import { getServerSession } from "next-auth";
import { signIn, signOut, useSession } from "next-auth/react";

export default function AuthButton() {
    const {data: session} = useSession();
    // const session = await getServerSession(); // also make the function async
    
    if(session){
        return (
            <>
                <br></br>
                <br></br>
                <br></br>
                <br></br>
                <p>signed in!</p>
                <button onClick={() => signOut()}>click here to sign out</button>
                <br></br>
                <br></br>
                <p>server session result:</p>
                <p> {`name: ${session?.user?.name}`}</p>
                <p> {`email: ${session?.user?.email}`}</p>
                <p> {`image: ${session?.user?.image}`}</p>
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