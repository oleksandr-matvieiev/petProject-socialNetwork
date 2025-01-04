import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import NavigationMenu from './NavigationMenu';

const UserProfile = () => {
    const { username } = useParams();
    const [user, setUser] = useState(null);
    const [posts, setPosts] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const apiBaseUrl = 'http://localhost:8080/api';
    const currentUsername = localStorage.getItem('username');

    const deletePost = async (postId) => {
        try {
            const token = localStorage.getItem('Token');
            await axios.delete(`${apiBaseUrl}/posts/deletePost/${postId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            setPosts(posts.filter(post => post.id !== postId));
        } catch (err) {
            console.error('Error deleting post:', err);
            alert('Failed to delete the post. Please try again.');
        }
    };


    useEffect(() => {
        const fetchUserData = async () => {
            try {
                setLoading(true);
                const [userResponse, postsResponse] = await Promise.all([
                    axios.get(`${apiBaseUrl}/user/${username}`),
                    axios.get(`${apiBaseUrl}/posts/userPosts/${username}`),
                ]);
                setUser(userResponse.data);
                setPosts(postsResponse.data);
                setError(null);
            } catch (err) {
                console.error('Error fetching user profile:', err);
                setError('Failed to load user profile. Please try again later.');
            } finally {
                setLoading(false);
            }
        };
        fetchUserData();
    }, [username]);

    if (loading) return <p>Loading...</p>;
    if (error) return <p style={{ color: 'red' }}>{error}</p>;

    return (
        <div>
            <NavigationMenu />
            <h1>Profile of {user.username}</h1>

            {user.profilePicture && (
                <img
                    src={`http://localhost:8080${user.profilePicture}`}
                    alt="Profile"
                    style={{
                        width: '150px',
                        height: '150px',
                        borderRadius: '50%',
                        objectFit: 'cover',
                    }}
                />
            )}

            <p>Email: {user.email}</p>
            <p>Bio: {user.bio || 'No bio available'}</p>

            {currentUsername === username && (
                <button
                    onClick={() => navigate(`/profile/edit`)}
                    style={{
                        marginTop: '20px',
                        padding: '10px 20px',
                        background: '#007bff',
                        color: 'white',
                        border: 'none',
                        borderRadius: '5px',
                        cursor: 'pointer',
                    }}
                >
                    Edit Profile
                </button>
            )}

            <h2>Posts:</h2>
            {posts.length > 0 ? (
                <ul>
                    {posts.map((post) => (
                        <li key={post.id} style={{ marginBottom: '20px' }}>
                            <p>{post.content}</p>
                            {post.imageUrl && (
                                <img
                                    src={`http://localhost:8080${post.imageUrl}`}
                                    alt="Post"
                                    style={{ maxWidth: '200px', marginTop: '10px' }}
                                />
                            )}
                            <small>Posted on: {new Date(post.createdAt).toLocaleString()}</small>
                            {currentUsername === post.user.username && (
                                <button
                                    onClick={() => deletePost(post.id)}
                                    style={{
                                        marginLeft: '10px',
                                        padding: '5px 10px',
                                        background: '#dc3545',
                                        color: 'white',
                                        border: 'none',
                                        borderRadius: '5px',
                                        cursor: 'pointer',
                                    }}
                                >
                                    Delete
                                </button>
                            )}
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No posts available.</p>
            )}
        </div>
    );
};

export default UserProfile;
