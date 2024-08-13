"use client"
// Dummy profile page

import React from 'react';
import { Button } from 'reactstrap';
import NavBar from '@/components/nav/navbars';
import { useSession } from 'next-auth/react';
import { signOut } from 'next-auth/react';
import authenticateComponent from '@/components/auth/authenticateComponent';

function Profile() {
    const { data: session } = useSession();

    const handleLogout = async () => {
        try {
            await signOut();
            console.log('Logout successful');
        } catch (error) {
            console.error('Logout failed:', error);
            alert('Logout failed: ' + error);
        }
    };

  return (
    <div className="h-screen w-screen">
      <NavBar></NavBar>
      <div>
        <h1>Profile</h1>
        {session && (
          <>
            <p>Profile</p>
            <p>{session.user.email}</p>
            <p>{session.user.id}</p>
            <Button color="primary" onClick={handleLogout}>Logout</Button>
          </>
        )}
      </div>
    </div>
  );
}

export default authenticateComponent(Profile);