import {useEffect, useState} from 'react';
import {useParams} from 'react-router-dom';
import axios from "axios";

const UserProfile = () => {
    const {username} = useParams();
    const [user, setUser] = useState(null);
    const [posts, setPosts] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    const apiBaseUrl = 'http://localhost:8080/api/user';

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                setLoading(true);

                // Запити на дані користувача і пости
                const [userResponse, postsResponse] = await Promise.all([
                    axios.get(`${apiBaseUrl}/${username}`),
                    axios.get(`${apiBaseUrl}/${username}/posts`)
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
    if (error) return <p style={{color: 'red'}}>{error}</p>;

    return (
        <div>
            <h1>Profile of {user.username}</h1>

            {user.profile_picture && (
                <img
                    src={`http://localhost:8080${user.profile_picture}`}
                    alt="Profile"
                    style={{width: '150px', height: '150px', borderRadius: '50%', objectFit: 'cover'}}
                />
            )}

            <p>Email: {user.email}</p>
            <p>Bio: {user.bio || 'No bio available'}</p>

            <h2>Posts:</h2>
            {posts.length > 0 ? (
                <ul>
                    {posts.map((post) => (
                        <li key={post.id} style={{marginBottom: '20px'}}>
                            <p>{post.content}</p>
                            {post.imageUrl && (
                                <img
                                    src={`http://localhost:8080${post.imageUrl}`}
                                    alt="Post"
                                    style={{maxWidth: '200px', marginTop: '10px'}}
                                />
                            )}
                            <small>Posted on: {new Date(post.createdAt).toLocaleString()}</small>
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
