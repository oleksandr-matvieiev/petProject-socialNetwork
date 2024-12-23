import React, {useEffect, useState} from 'react';
import axios from 'axios';

const HomePage = () => {
    const [posts, setPosts] = useState([]);
    const [error, setError] = useState(null);
    const [search, setSearch] = useState("");
    const [loading, setLoading] = useState(false);
    const apiBaseUrl = 'http://localhost:8080/api/posts';

    const fetchPosts = async () => {
        setLoading(true);
        try {
            const response = await axios.get(`${apiBaseUrl}/allPosts`);
            setPosts(response.data);
            setError(null);
        } catch (err) {
            console.error('Error fetching posts:', err);
            setError('Failed to fetch posts. Please try again later.');
        } finally {
            setLoading(false);
        }
    };

    const toggleLike = async (postId) => {
        const token = localStorage.getItem('Token');
        try {
            const response = await axios.post(
                `http://localhost:8080/api/posts/${postId}/like`,
                {},
                { headers: { Authorization: `Bearer ${token}` } }
            );

            const updatedPosts = posts.map((post) => {
                if (post.id === postId) {
                    return {
                        ...post,
                        likedByCurrentUser: !post.likedByCurrentUser,
                        likeCount: post.likedByCurrentUser
                            ? post.likeCount - 1
                            : post.likeCount + 1,
                    };
                }
                return post;
            });
            setPosts(updatedPosts);
        } catch (err) {
            console.error('Error liking post:', err);
        }
    };



    const searchPosts = async (username) => {
        if (!username) {
            fetchPosts();
            return;
        }
        setLoading(true);
        try {
            const response = await axios.get(`${apiBaseUrl}/userPosts/${username}`);
            setPosts(response.data);
            setError(null);
        } catch (err) {
            console.error('Error fetching posts for user:', err);
            setError(`No posts found for username: ${username}`);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchPosts();
    }, []);

    return (
        <div>
            <h1>Welcome to the Home Page</h1>
            <div style={{marginBottom: '20px'}}>
                <input
                    type="text"
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                    placeholder="Search posts by username"
                    style={{marginRight: '10px', padding: '5px'}}
                />
                <button onClick={() => searchPosts(search)} style={{padding: '5px 10px'}}>
                    Search
                </button>
            </div>
            {loading && <p>Loading...</p>}
            {error && <p style={{color: 'red'}}>{error}</p>}
            {posts.length > 0 ? (
                <div>
                    <h2>{search ? `Posts by "${search}"` : "All Posts"}:</h2>
                    <ul>
                        {posts.map((post) => (
                            <li key={post.id} style={{marginBottom: '20px'}}>
                                <h3>{post.user.username}</h3>
                                <p>{post.content}</p>
                                {post.imageUrl && (
                                    <img
                                        src={`http://localhost:8080${post.imageUrl}`}
                                        alt="Post"
                                        style={{maxWidth: '200px', marginTop: '10px'}}
                                    />
                                )}
                                <small>Posted on: {new Date(post.createdAt).toLocaleString()}</small>
                                <button
                                    onClick={() => toggleLike(post.id)}
                                    style={{marginTop: '10px', padding: '5px 10px'}}
                                >
                                    {post.likedByCurrentUser ? "Unlike" : "Like"}
                                </button>
                                <p>{post.likeCount} {post.likeCount === 1 ? 'like' : 'likes'}</p>
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
