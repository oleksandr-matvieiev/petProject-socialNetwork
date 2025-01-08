import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import NavigationMenu from '../NavigationMenu';

const UserProfile = () => {
    const { username } = useParams();
    const [user, setUser] = useState(null);
    const [posts, setPosts] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [isFollowing, setIsFollowing] = useState(false);
    const [followStats, setFollowStats] = useState({ followers: 0, following: 0 });
    const navigate = useNavigate();
    const apiBaseUrl = 'http://localhost:8080/api';
    const currentUsername = localStorage.getItem('username');
    const token = localStorage.getItem('token');

    const handleFollowToggle = async () => {
        try {
            const method = isFollowing ? 'DELETE' : 'POST';
            await axios({
                method,
                url: `${apiBaseUrl}/follow/${username}`,
                headers: { Authorization: `Bearer ${token}` },
            });

            setIsFollowing(!isFollowing);
            setFollowStats((prev) => ({
                ...prev,
                followers: isFollowing ? prev.followers - 1 : prev.followers + 1,
            }));
        } catch (err) {
            console.error('Error toggling follow:', err);
        }
    };

    const deletePost = async (postId) => {
        try {
            await axios.delete(`${apiBaseUrl}/posts/deletePost/${postId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setPosts(posts.filter((post) => post.id !== postId));
        } catch (err) {
            console.error('Error deleting post:', err);
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
            } catch (err) {
                setError('Failed to load user profile.');
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        const fetchFollowStatus = async () => {
            try {
                const response = await axios.get(`${apiBaseUrl}/follow/${currentUsername}/following`, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                setIsFollowing(response.data.some((f) => f.followee.username === username));
            } catch (err) {
                console.error('Error fetching follow status:', err);
            }
        };

        const fetchFollowCounts = async () => {
            try {
                const [followersResponse, followingResponse] = await Promise.all([
                    axios.get(`${apiBaseUrl}/follow/${username}/followers`, {
                        headers: { Authorization: `Bearer ${token}` },
                    }),
                    axios.get(`${apiBaseUrl}/follow/${username}/following`, {
                        headers: { Authorization: `Bearer ${token}` },
                    }),
                ]);

                setFollowStats({
                    followers: followersResponse.data.length,
                    following: followingResponse.data.length,
                });
            } catch (err) {
                console.error('Error fetching follow counts:', err);
            }
        };

        fetchUserData();
        fetchFollowStatus();
        fetchFollowCounts();
    }, [username, currentUsername, token]);

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

            <button onClick={handleFollowToggle} style={{ margin: '10px' }}>
                {isFollowing ? 'Unfollow' : 'Follow'}
            </button>
            <div>
                <p>Followers: {followStats.followers}</p>
                <p>Following: {followStats.following}</p>
            </div>

            {currentUsername === username && (
                <button onClick={() => navigate(`/profile/edit`)}>Edit Profile</button>
            )}

            <h2>Posts:</h2>
            {posts.length > 0 ? (
                <ul>
                    {posts.map((post) => (
                        <li key={post.id}>
                            <p>{post.content}</p>
                            {post.imageUrl && <img src={`http://localhost:8080${post.imageUrl}`} alt="Post" />}
                            {currentUsername === post.user.username && (
                                <button onClick={() => deletePost(post.id)}>Delete</button>
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
