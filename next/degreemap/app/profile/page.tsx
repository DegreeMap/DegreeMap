import AuthButton from "@/components/auth/buttons";
import NavBar from "@/components/nav/navbars";
import React, { useEffect, useState } from "react";

export default function ProfilePage() {    
    return (
        <>
            <NavBar></NavBar>
            <p>profile page!</p>

            <AuthButton></AuthButton>
        </>
    );
}