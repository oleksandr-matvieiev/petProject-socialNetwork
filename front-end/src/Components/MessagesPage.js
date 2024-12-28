import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './MessagePage.css'; // Додатковий CSS файл для стилізації

const MessagesPage = () => {
    const [inbox, setInbox] = useState([]);
    const [conversation, setConversation] = useState([]);
    const [receiver, setReceiver] = useState(""); // Для ручного введення отримувача
    const [currentChatUser, setCurrentChatUser] = useState(""); // Поточний співрозмовник
    const [currentUser, setCurrentUser] = useState(""); // Поточний користувач
    const [content, setContent] = useState("");

    const apiBaseUrl = 'http://localhost:8080/api/messages';

    useEffect(() => {
        fetchInbox();
        fetchCurrentUser();
    }, []);

    const fetchInbox = async () => {
        const token = localStorage.getItem("Token");
        try {
            const response = await axios.get(`${apiBaseUrl}/inbox`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setInbox(response.data);
        } catch (error) {
            console.error("Error fetching inbox:", error);
        }
    };

    const fetchCurrentUser = async () => {
        const token = localStorage.getItem("Token");
        try {
            const response = await axios.get('http://localhost:8080/api/auth/me', {
                headers: { Authorization: `Bearer ${token}` },
            });
            const username = response.data.replace('username', '').trim(); // Видаляємо "username" з API
            setCurrentUser(username);
        } catch (error) {
            console.error("Error fetching current user:", error);
        }
    };

    const fetchConversation = async (receiverUsername) => {
        const token = localStorage.getItem("Token");
        try {
            const response = await axios.get(`${apiBaseUrl}/conversation/${receiverUsername}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setConversation(response.data);
            setCurrentChatUser(receiverUsername);
            setReceiver(receiverUsername);

            response.data.forEach(async (msg) => {
                if (!msg.isRead && msg.receiverUsername === currentUser) {
                    await axios.post(`${apiBaseUrl}/${msg.id}/read`, {}, {
                        headers: { Authorization: `Bearer ${token}` },
                    });
                }
            });
        } catch (error) {
            console.error("Error fetching conversation:", error);
        }
    };

    const sendMessage = async () => {
        const token = localStorage.getItem("Token");
        if (!receiver || !content) {
            alert("Please enter a receiver and a message.");
            return;
        }
        try {
            await axios.post(`${apiBaseUrl}/send`, { receiver, content }, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setContent("");
            fetchConversation(receiver);
        } catch (error) {
            console.error("Error sending message:", error);
        }
    };

    return (
        <div className="messages-page">
            <h1>Messages</h1>

            <div className="inbox">
                <h2>Inbox</h2>
                {inbox.length ? (
                    <ul>
                        {inbox.map((message) => (
                            <li
                                key={message.id}
                                className={message.isRead ? "message-read" : "message-unread"}
                                onClick={() => fetchConversation(message.senderUsername)}
                            >
                                <strong>{message.senderUsername}</strong>: {message.content}
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p>No messages in your inbox.</p>
                )}
            </div>

            <div className="conversation">
                <h2>Conversation with {currentChatUser || "..."}</h2>
                <div className="chat">
                    {conversation.map((msg) => (
                        <div
                            key={msg.id}
                            className={msg.senderUsername === currentUser ? "message sent" : "message received"}
                        >
                            <p style={{ fontWeight: !msg.isRead && msg.receiverUsername === currentUser ? "bold" : "normal" }}>
                                <strong>{msg.senderUsername === currentUser ? "You" : msg.senderUsername}:</strong> {msg.content}
                            </p>
                        </div>
                    ))}
                </div>
            </div>

            <div className="message-form">
                <h2>Send a Message</h2>
                <input
                    type="text"
                    placeholder="Receiver's username"
                    value={receiver}
                    onChange={(e) => setReceiver(e.target.value)}
                />
                <textarea
                    placeholder="Type your message..."
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                />
                <button onClick={sendMessage}>Send</button>
            </div>
        </div>
    );
};

export default MessagesPage;
