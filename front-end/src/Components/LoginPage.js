import React, { useState } from 'react';
import axios from 'axios';

const AuthPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [error, setError] = useState(null);
    const [isRegistering, setIsRegistering] = useState(false);

    const apiBaseUrl = 'http://localhost:8080/api/auth';

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post(`${apiBaseUrl}/login`, { username, password });
            const Token  = response.data.token;

            if (Token) {
                localStorage.setItem('Token', Token);
                console.log('Login successful. Token:', Token);
                setError(null);
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
            const response = await axios.post(`${apiBaseUrl}/register`, { username, email, password });
            console.log('Registration successful:', response.data);
            alert('Registration successful! Please login.');
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
        alert('Logged out successfully.');
    };

    return (
        <div>
            <h1>{isRegistering ? 'Register' : 'Login'}</h1>

            {error && <p style={{ color: 'red' }}>{error}</p>}

            <form onSubmit={isRegistering ? handleRegister : handleLogin}>
                <input
                    type="text"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
                {isRegistering && (
                    <input
                        type="email"
                        placeholder="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                )}
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <button type="submit">{isRegistering ? 'Register' : 'Login'}</button>
            </form>

            <button onClick={() => setIsRegistering(!isRegistering)}>
                {isRegistering ? 'Already have an account? Login' : 'New here? Register'}
            </button>

            {isAuthenticated() && (
                <button onClick={handleLogout} style={{ marginTop: '10px' }}>
                    Logout
                </button>
            )}
        </div>
    );
};

export default AuthPage;
