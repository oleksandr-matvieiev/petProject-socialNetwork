import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const VerifyEmailPage = () => {
    const [email, setEmail] = useState('');
    const [code, setCode] = useState('');
    const [message, setMessage] = useState('');
    const navigate=useNavigate();

    useEffect(() => {
        const savedEmail = localStorage.getItem('emailToVerify');
        if (savedEmail) {
            setEmail(savedEmail);
        }
    }, []);

    const handleVerify = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/api/auth/verify-email', { email, code });
            setMessage(response.data);
            alert("Verification success!")
            localStorage.removeItem('emailToVerify');
            setTimeout(() => {
                navigate('/login');
            }, 2000);
        } catch (err) {
            setMessage('Verification failed. Please try again.');
        }
    };

    return (
        <div>
            <h2>Email Verification</h2>
            <form onSubmit={handleVerify}>
                <input
                    type="email"
                    placeholder="Enter your email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                <input
                    type="text"
                    placeholder="Enter verification code"
                    value={code}
                    onChange={(e) => setCode(e.target.value)}
                />
                <button type="submit">Verify</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default VerifyEmailPage;
