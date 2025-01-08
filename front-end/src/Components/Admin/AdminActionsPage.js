import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {useNavigate, useParams} from 'react-router-dom';
import NavigationMenu from "../NavigationMenu";

const AdminActionsPage = () => {
    const {username} = useParams();
    const [user, setUser] = useState(null);
    const [emailContent, setEmailContent] = useState('');
    const [emailSubject, setEmailSubject] = useState('');
    const navigate = useNavigate();

    const apiBaseUrl = 'http://localhost:8080/api/admin';
const token=localStorage.getItem('token');

    useEffect(() => {
        fetchUser();
    }, []);

    const fetchUser = async () => {
        try {
            const response = await axios.get(`${apiBaseUrl}/actions/view-account-info/{username}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            setUser(response.data);
        } catch (err) {
            console.error('Error fetching user details:', err);
        }
    };

    const handleDelete = async () => {
        try {
            await axios.delete(`${apiBaseUrl}/actions/delete-account/${username}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            alert('User account deleted successfully.');
            navigate('/admin/users');
        } catch (err) {
            console.error('Error deleting user:', err);
        }
    };

    const handleSendEmail = async () => {
        try {
            await axios.post(`${apiBaseUrl}/actions/send-email/${username}`, {
                subject: emailSubject,
                content: emailContent,
            }, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            alert('Email sent successfully.');
        } catch (err) {
            console.error('Error sending email:', err);
        }
    };

    const handleViewAccount = () => {
        navigate(`/profile/${username}`);
    };

    return (
        <div>
            <NavigationMenu />
            <h1>Actions for {username}</h1>
            {user && (
                <div>
                    <p>Email: {user.email}</p>
                    <p>Bio: {user.bio}</p>
                </div>
            )}
            <div>
                <input
                    type="text"
                    placeholder="Email Subject"
                    value={emailSubject}
                    onChange={(e) => setEmailSubject(e.target.value)}
                />
                <textarea
                    placeholder="Email Content"
                    value={emailContent}
                    onChange={(e) => setEmailContent(e.target.value)}
                />
                <button onClick={handleSendEmail}>Send Email</button>
            </div>
            <button onClick={handleViewAccount}>View Account</button>
            <button onClick={handleDelete}>Delete Account</button>
        </div>
    );
};

export default AdminActionsPage;
