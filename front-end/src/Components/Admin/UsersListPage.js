import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {Link} from 'react-router-dom';
import NavigationMenu from "../NavigationMenu";

const UsersListPage = () => {
    const [users, setUsers] = useState([]);
    const [search, setSearch] = useState('');

    const apiBaseUrl = 'http://localhost:8080/api/admin';
    const token = localStorage.getItem('token');

    useEffect(() => {
        fetchUsers();
    }, [search]);

    const fetchUsers = async () => {
        try {
            const response = await axios.get(`${apiBaseUrl}/get-all-users`, {
                params: {search},
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            setUsers(response.data);
        } catch (err) {
            console.error('Error fetching users:', err);
        }
    };

    return (
        <div>
            <NavigationMenu />
            <h1>Users List</h1>
            <input
                type="text"
                placeholder="Search users..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
            />
            <ul>
                {users.map((user) => (
                    <li key={user.username}>
                        <Link to={`/admin/actions/${user.username}`}>
                            {user.username}
                        </Link>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default UsersListPage;
