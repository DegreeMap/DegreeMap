import Link from 'next/link';
import React from 'react';

interface NavItemProps {
    navItem: {
        name: string;
        link: string;
    };
}

const NavItem: React.FC<NavItemProps> = ({navItem}) => {
    
    return (
        <div className="nav-item">
            <Link href={navItem.link}>
              {navItem.name}
            </Link>
        </div>
    );
}

export default NavItem;