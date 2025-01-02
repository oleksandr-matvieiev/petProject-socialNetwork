import React, {useState} from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import NavigationMenu from "./NavigationMenu";

const AuthPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [bio, setBio] = useState('');
    const [profilePhoto, setProfilePhoto] = useState(null);
    const [error, setError] = useState(null);
    const [isRegistering, setIsRegistering] = useState(false);

    const navigate=useNavigate();
    const apiBaseUrl = 'http://localhost:8080/api/auth';

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post(`${apiBaseUrl}/login`, {username, password});
            const Token = response.data.token;

            if (Token) {
                localStorage.setItem('Token', Token);
                localStorage.setItem('username',username);
                setError(null);
                alert("Login successful!")
                navigate("/");
            } else {
                setError('Invalid response from server. No token provided.');
            }
        } catch (err) {
            console.error('Login failed:', err);
            setError('Login failed. Please check your credentials.');
        }
    };

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            const formData = new FormData();
            formData.append("username", username);
            formData.append("email", email);
            formData.append("password", password);
            formData.append("bio", bio);
            if (profilePhoto) {
                formData.append("image", profilePhoto)
            }
            const response = await axios.post(`${apiBaseUrl}/register`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                },
            });
            console.log('Registration successful:', response.data);
            alert('Registration successful! Please login.');
            setProfilePhoto(null);
            setIsRegistering(false);
            setError(null);
        } catch (err) {
            console.error('Registration failed:', err);
            setError('Registration failed. Please try again.');
        }
    };

    const isAuthenticated = () => {
        return !!localStorage.getItem('Token');
    };

    const handleLogout = () => {
        localStorage.removeItem('Token');
        localStorage.removeItem('roles');
        localStorage.removeItem('username')
        alert('Logged out successfully.');
    };

    return (
        <div>
            <NavigationMenu/>
            <h1>{isRegistering ? 'Register' : 'Login'}</h1>

            {error && <p style={{color: 'red'}}>{error}</p>}

            <form onSubmit={isRegistering ? handleRegister : handleLogin}>
                <input
                    type="text"
                    placeholder="Enter your username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
                {isRegistering && (
                    <input
                        type="email"
                        placeholder="Enter your email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                )}
                <input
                    type="password"
                    placeholder="Enter your password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                {isRegistering && (
                    <input
                        type="text"
                        placeholder={"Enter your bio"}
                        value={bio}
                        onChange={(e) => setBio(e.target.value)}
                    />)}
                {isRegistering && (

                    <input
                        type="file"
                        placeholder="Upload your profile foto"
                        onChange={(e) => setProfilePhoto(e.target.files[0])}
                    />)}

                <button type="submit">{isRegistering ? 'Register' : 'Login'}</button>
            </form>

            <button onClick={() => setIsRegistering(!isRegistering)}>
                {isRegistering ? 'Already have an account? Login' : 'New here? Register'}
            </button>

            {isAuthenticated() && (
                <button onClick={handleLogout} style={{marginTop: '10px'}}>
                    Logout
                </button>
            )}
        </div>
    );
};

export default AuthPage;
