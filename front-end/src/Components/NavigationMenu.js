import React from 'react';
import { Link } from 'react-router-dom';
import './NavigationMenu.css';

const NavigationMenu = () => {
    return (
        <nav className="navigation-menu">
            <ul>
                <li>
                    <Link to="/">Home</Link>
                </li>
                <li>
                    <Link to="/messages">Messages</Link>
                </li>
                <li>
                    <Link to="/profile">Profile</Link>
                </li>
            </ul>
        </nav>
    );
};

export default NavigationMenu;
