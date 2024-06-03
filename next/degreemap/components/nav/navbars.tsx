"use client"

import React from 'react';
import NavItem from './navitem';
import { useSession } from 'next-auth/react';

type NavBarProps = {
    navItems?: NavData[]
}

type NavData = {
    name: string;
    link: string;
};

export default function NavBar({navItems}: NavBarProps) {
    const { data: session } = useSession();
    
    var defaultNavData: NavData[];
    if(session) {
        defaultNavData = [
            { name: "Maker", link: "/maker" },
            { name: "Courses", link: "/courses" },
            { name: "Profile", link: "/profile" },
        ];
    }
    else {
        defaultNavData = [
            { name: "Maker", link: "/maker" },
            { name: "Courses", link: "/courses" },
            { name: "Sign In", link: "/profile" },
        ];
    }

    const items = navItems || defaultNavData; // If no props are given, a default navbar will be made.
    
    return (
        <div className="flex flex-row items-center w-full h-20 p-3.5 px-32 bg-slate-400">
            <NavItem key={'logo'} navData={{name: "Homepage/Logo", link: "/" }}></NavItem>
            <div className="flex-1"></div>
            {items.map((item, index) => (
                <NavItem key={index} navData={item}></NavItem>
            ))}    
        </div>
    );
}
