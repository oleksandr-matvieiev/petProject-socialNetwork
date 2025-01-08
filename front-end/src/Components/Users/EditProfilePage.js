import React, { useState } from 'react';
import axios from 'axios';
import NavigationMenu from '../NavigationMenu';

const EditProfilePage = () => {
    const [bio, setBio] = useState('');
    const [profilePicture, setProfilePicture] = useState(null);
    const [message, setMessage] = useState('');
    const apiBaseUrl = 'http://localhost:8080/api/user';

    const handleFileChange = (event) => {
        setProfilePicture(event.target.files[0]);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        const formData = new FormData();
        formData.append('bio', bio);
        if (profilePicture) {
            formData.append('newPicture', profilePicture);
        }

        const token = localStorage.getItem('token');

        try {
            const response = await axios.post(
                `${apiBaseUrl}/edit/profile-info`,
                formData,
                { headers: { Authorization: `Bearer ${token}` } }
            );
            setMessage('Profile updated successfully!');
        } catch (error) {
            console.error('Error updating profile:', error);
            setMessage('Failed to update profile. Please try again.');
        }
    };

    return (
        <div className="page-container">
            <NavigationMenu />
            <h1>Edit Profile</h1>
            <form onSubmit={handleSubmit} className="edit-profile-form">
                <div className="form-group">
                    <label htmlFor="bio">Bio:</label>
                    <textarea
                        id="bio"
                        value={bio}
                        onChange={(e) => setBio(e.target.value)}
                        placeholder="Tell us about yourself"
                        rows="4"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="profilePicture">Profile Picture:</label>
                    <input
                        type="file"
                        id="profilePicture"
                        onChange={handleFileChange}
                    />
                </div>
                <button type="submit" className="submit-button">
                    Save Changes
                </button>
            </form>
            {message && <p className="message">{message}</p>}
        </div>
    );
};

export default EditProfilePage;
