import Link from 'next/link';
import React from 'react';

interface NavItemProps {
    navData: {
        name: string;
        link: string;
    };
}

const NavItem: React.FC<NavItemProps> = ({navData}) => {
    return (
        <div className="mx-3.5">
            <Link href={navData.link}>
              {navData.name}
            </Link>
        </div>
    );
}

export default NavItem;