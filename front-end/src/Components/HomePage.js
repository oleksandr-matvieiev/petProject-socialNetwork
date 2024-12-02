import React, {useEffect, useState} from 'react';
import axios from 'axios';

const HomePage = () => {
    const [posts, setPosts] = useState([]);
    const [error, setError] = useState(null);
    const apiBaseUrl = 'http://localhost:8080/api/posts';

    const fetchPosts = async () => {
        try {
            const response = await axios.get(`${apiBaseUrl}/allPosts`);
            setPosts(response.data);
        } catch (err) {
            console.error('Error fetching posts:', err);
            setError('Failed to fetch posts. Please try again later.');
        }
    };

    useEffect(() => {
        fetchPosts();
    }, []);

    return (
        <div>
            <h1>Welcome to the Home Page</h1>
            {error && <p style={{color: 'red'}}>{error}</p>}
            {posts.length > 0 ? (
                <div>
                    <h2>All Posts:</h2>
                    <ul>
                        {posts.map((post) => (
                            <li key={post.id} style={{marginBottom: '20px'}}>
                                <h3>{post.user.username}</h3>
                                <p>{post.content}</p>
                                {post.imageUrl && (
                                    <img
                                        src={post.imageUrl}
                                        alt="Post"
                                        style={{maxWidth: '200px', marginTop: '10px'}}
                                    />
                                )}
                                <small>Posted on: {new Date(post.createdAt).toLocaleString()}</small>
                            </li>
                        ))}
                    </ul>
                </div>
            ) : (
                <p>No posts available. Be the first to create a post!</p>
            )}
        </div>
    );
};

export default HomePage;
