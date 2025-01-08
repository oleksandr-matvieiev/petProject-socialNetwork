import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './MessagePage.css';
import NavigationMenu from "../NavigationMenu";


const MessagesPage = () => {
    const [chats, setChats] = useState([]);
    const [conversation, setConversation] = useState([]);
    const [receiver, setReceiver] = useState("");
    const [currentChatUser, setCurrentChatUser] = useState("");
    const [currentUser, setCurrentUser] = useState("");
    const [content, setContent] = useState("");

    const apiBaseUrl = 'http://localhost:8080/api';

    useEffect(() => {
        fetchChats();
        fetchCurrentUser();
    }, []);

    const fetchChats = async () => {
        const token = localStorage.getItem("token");
        try {
            const response = await axios.get(`${apiBaseUrl}/chats`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setChats(response.data);
        } catch (error) {
            console.error("Error fetching chats:", error);
        }
    };

    const fetchCurrentUser = async () => {
        const token = localStorage.getItem("token");
        try {
            const response = await axios.get(`${apiBaseUrl}/auth/me`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setCurrentUser(response.data.replace('username', '').trim());
        } catch (error) {
            console.error("Error fetching current user:", error);
        }
    };

    const fetchConversation = async (receiverUsername) => {
        const token = localStorage.getItem("token");
        try {
            const response = await axios.get(`${apiBaseUrl}/messages/conversation/${receiverUsername}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setConversation(response.data);
            setCurrentChatUser(receiverUsername);
            setReceiver(receiverUsername);

            // Відзначити непрочитані повідомлення
            response.data.forEach(async (msg) => {
                if (!msg.isRead && msg.receiverUsername === currentUser) {
                    await axios.post(`${apiBaseUrl}/messages/${msg.id}/read`, {}, {
                        headers: { Authorization: `Bearer ${token}` },
                    });
                }
            });
        } catch (error) {
            console.error("Error fetching conversation:", error);
        }
    };

    const createNewChat = async () => {
        const token = localStorage.getItem("token");
        if (!receiver) {
            alert("Please enter a username to start a chat.");
            return;
        }
        try {
            const response = await axios.post(`${apiBaseUrl}/chats/create`, null, {
                params: { receiverUsername: receiver },
                headers: { Authorization: `Bearer ${token}` },
            });
            setChats([...chats, response.data]); // Додаємо новий чат у список
            setConversation([]);
            setCurrentChatUser(receiver);
        } catch (error) {
            console.error("Error creating a chat:", error);
        }
    };

    const sendMessage = async () => {
        const token = localStorage.getItem("token");
        if (!receiver || !content) {
            alert("Please enter a receiver and a message.");
            return;
        }
        try {
            await axios.post(`${apiBaseUrl}/messages/send`, { receiver, content }, {
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

            <div className="chat-list">
                <NavigationMenu />
                <h2>Your Chats</h2>
                {chats.length ? (
                    <ul>
                        {chats.map((chat) => {
                            const otherUser = chat.userOne === currentUser ? chat.userTwo : chat.userOne;
                            return (
                                <li key={chat.id} onClick={() => fetchConversation(otherUser)}>
                                    {otherUser}
                                </li>
                            );
                        })}
                    </ul>
                ) : (
                    <p>No chats available.</p>
                )}
                <div>
                    <input
                        type="text"
                        placeholder="Enter username for new chat"
                        value={receiver}
                        onChange={(e) => setReceiver(e.target.value)}
                    />
                    <button onClick={createNewChat}>Start New Chat</button>
                </div>
            </div>

            <div className="conversation">
                <h2>Conversation with {currentChatUser || "New Chat"}</h2>
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
