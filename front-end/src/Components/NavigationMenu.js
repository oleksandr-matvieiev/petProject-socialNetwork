import React, {useEffect, useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import './NavigationMenu.css';

const NavigationMenu = () => {
    const [username, setUsername] = useState(null);

    const navigate = useNavigate();

    useEffect(() => {
        const storedUsername = localStorage.getItem('username');
        setUsername(storedUsername);
    });

    const handleLogout = () => {
        localStorage.removeItem('roles');
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        navigate("/");
    }

    return (
        <div className="navigation-container">
            <nav className="navigation-menu">
                <ul>
                    <li>
                        <Link to="/">Home</Link>
                    </li>
                    {username&& (
                        <li>
                            <Link to="/messages">Messages</Link>
                        </li>
                    )}
                    {username && (
                        <li>
                        <Link to={`/profile/${username}`}>Profile</Link>
                        </li>
                    )}
                    {!username&&(
                        <li>
                            <Link to={`/login`}>Registration / Login</Link>
                        </li>
                    )}
                    {username&&(
                        <Link to={`/post/create`}>Create Post</Link>
                    )}
                    {username&&(
                        <Link to={`/notifications`}>Notifications</Link>
                    )}
                </ul>
            </nav>
            {username && (
                <button className="logout-button" onClick={handleLogout}>
                    Logout
                </button>
            )}
        </div>
    );
};

export default NavigationMenu;
