import React, {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';
import './NavigationMenu.css';

const NavigationMenu = () => {
    const [username, setUsername] = useState(null);

    useEffect(() => {
        const storedUsername = localStorage.getItem('username');
        setUsername(storedUsername);
    });

    return (
        <nav className="navigation-menu">
            <ul>
                <li>
                    <Link to="/">Home</Link>
                </li>
                <li>
                    <Link to="/messages">Messages</Link>
                </li>
                {username && (
                    <li>
                        <Link to={`/profile/${username}`}>Profile</Link>
                    </li>
                )}

            </ul>
        </nav>
    );
};

export default NavigationMenu;
