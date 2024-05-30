import React from 'react';
import NavItem from './navitem';

// Define the type for navigation items
type NavItemData = {
    name: string;
    link: string;
};

export default function NavBar() {
    const leftNavData: NavItemData[] = [
        { name: "Homepage/Logo", link: "/" },
    ];
    
    const navData: NavItemData[] = [
        { name: "Profile", link: "/profile" },
        { name: "Maker", link: "/maker" },
        { name: "Courses", link: "/courses" }
    ];
    
    return (
        <div className="flex flex-row w-full h-20 p-3.5 bg-slate-400">
            {leftNavData.map((item, index) => (
                <NavItem key={`left-${index}`} navItem={item}></NavItem>   
            ))}
            <div className="flex-1"></div>
            {navData.map((item, index) => (
                <NavItem key={index} navItem={item}></NavItem>
            ))}    
        </div>
    );
}
