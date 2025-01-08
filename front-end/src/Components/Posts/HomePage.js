import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {Link} from "react-router-dom";
import './HomePage.css';
import NavigationMenu from "../NavigationMenu";


const HomePage = () => {
    const [posts, setPosts] = useState([]);
    const [error, setError] = useState(null);
    const [search, setSearch] = useState("");
    const [loading, setLoading] = useState(false);
    const [comments, setComments] = useState({});
    const [newComment, setNewComment] = useState("");
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
    const fetchComments = async (postId) => {
        try {
            const response = await axios.get(`${apiBaseUrl}/${postId}/comments`);
            setComments((prevComments) => ({
                ...prevComments, [postId]: response.data,
            }));
        } catch (err) {
            console.error(`Error fetching comments for post ${postId}:`, err);
        }
    };
    const addComment = async (postId) => {
        const token = localStorage.getItem("token");
        try {
            const response = await axios.post(
                `http://localhost:8080/api/posts/${postId}/addComment`,
                {content: newComment},
                {headers: {Authorization: `Bearer ${token}`}}
            );
            setComments((prevComments) => ({
                ...prevComments,
                [postId]: [...(prevComments[postId] || []), response.data],
            }));
            setNewComment("")
        } catch (err) {
            console.error(`Error adding comment to post ${postId}:`, err);
        }
    };


    const toggleLike = async (postId) => {
        const token = localStorage.getItem('token');
        try {
            const response = await axios.post(
                `http://localhost:8080/api/posts/${postId}/like`,
                {},
                {headers: {Authorization: `Bearer ${token}`}}
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
        <div className="page-container">
            <NavigationMenu/>
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
                            <li key={post.id} className="post-item">
                                <div style={{display: "flex", alignItems: "center", marginBottom: "10px"}}>
                                    <img
                                        src={`http://localhost:8080${post.user.profilePicture}`}
                                        alt={`${post.user.username}'s avatar`}
                                        style={{
                                            width: "40px",
                                            height: "40px",
                                            borderRadius: "50%",
                                            marginRight: "10px",
                                        }}
                                    />
                                    <h3>
                                        <Link to={`/profile/${post.user.username}`}
                                              style={{textDecoration: 'none', color: '#007bff'}}>
                                            {post.user.username}
                                        </Link>
                                    </h3>
                                </div>
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
                                <button
                                    onClick={() => fetchComments(post.id)}
                                    style={{marginTop: "10px", padding: "5px 10px"}}
                                >
                                    View Comments
                                </button>
                                {comments[post.id] && (
                                    <ul style={{marginTop: "10px"}}>
                                        {comments[post.id].map((comment) => (
                                            <li key={comment.id}>
                                                <p>{comment.user.username}: {comment.content}</p>
                                            </li>
                                        ))}
                                    </ul>
                                )}

                                <input
                                    type="text"
                                    value={newComment}
                                    onChange={(e) => setNewComment(e.target.value)}
                                    placeholder="Add a comment"
                                    style={{marginTop: "10px", padding: "5px", width: "80%"}}
                                />
                                <button
                                    onClick={() => addComment(post.id)}
                                    style={{padding: "5px 10px", marginTop: "10px"}}
                                >
                                    Add Comment
                                </button>
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
