import React, {useEffect, useState} from "react";
import axios from "axios";
import NavigationMenu from "./NavigationMenu";

const NotificationsPage = () => {
    const [notifications, setNotifications] = useState([]);
    const [loading, setLoading] = useState(true);

    const apiBaseUrl = 'http://localhost:8080/api/notifications';

    const token = localStorage.getItem('Token');
    useEffect(() => {
        const fetchNotifications = async () => {
            try {
                const response = await axios.get(`${apiBaseUrl}`,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`
                        }
                    });
                setNotifications(response.data);
                setLoading(false);
            } catch (error) {
                console.error("Error fetching notifications:", error);
                setLoading(false);
            }
        };

        fetchNotifications();
    }, []);

    const markAsRead = async (notificationId) => {
        try {
            await axios.post(`${apiBaseUrl}/${notificationId}/read`, {},
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
            setNotifications((prevNotifications) =>
                prevNotifications.map((notification) =>
                    notification.id === notificationId ? {...notification, read: true} : notification
                )
            );
        } catch (error) {
            console.error("Error marking notification as read:", error);
        }
    };
    const markAllAsRead = async () => {
        try {
            await axios.post(`${apiBaseUrl}/readAll`, {}, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            setNotifications((prevNotifications) =>
                prevNotifications.map((notification) =>
                    notification.id ? {...notification, read: true} : notification
                )
            );
        } catch (error) {
            console.error("Error marking all notifications as read: ", error)
        }
    };

    if (loading) return <p>Loading notifications...</p>;

    return (
        <div className="notifications-container">
            <NavigationMenu/>
            <h2>Notifications</h2>
            {notifications.length === 0 ? (
                <p>No notifications yet.</p>
            ) : (
                <ul className="notifications-list">
                    <button onClick={() => markAllAsRead()}>Mark all as Read</button>
                    {notifications.map((notification) => (
                        <li key={notification.id}
                            className={`notification-item ${notification.read ? "read" : "unread"}`}>
                            <p>{notification.message}</p>
                            <small>{new Date(notification.createdAt).toLocaleString()}</small>
                            {!notification.read && (
                                <button onClick={() => markAsRead(notification.id)}>Mark as Read</button>
                            )}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default NotificationsPage;
