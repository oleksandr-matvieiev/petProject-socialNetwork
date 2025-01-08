import React, {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate, useParams} from "react-router-dom";

const UserPostsPage = () => {
    const {username} = useParams();
    const [posts, setPosts] = useState([]);

    const apiBaseUrl = 'http://localhost:8080/api/admin';
    const token = localStorage.getItem('token');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchPosts = async () => {
            try {
                const response = await axios.get(`${apiBaseUrl}/actions/posts/${username}`, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
                setPosts(response.data);
            } catch (err) {
                console.error("Failed to fetch posts:", err);
            }
        };
        fetchPosts();
    }, [username]);

    const handleDeletePost = async (postId) => {
        try {
            await axios.delete(`${apiBaseUrl}/actions/posts/${username}/${postId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            setPosts(posts.filter(post => post.id !== postId));
            alert("Post deleted successfully!");
        } catch (err) {
            console.error("Failed to delete post:", err);
            alert("Failed to delete post.");
        }
    };

    return (
        <div>
            <h1>{username}'s Posts</h1>
            {posts.length === 0 ? (
                <p>No posts found.</p>
            ) : (
                <div>
                    {posts.map(post => (
                        <div key={post.id} style={{marginBottom: "20px", border: "1px solid #ccc", padding: "10px"}}>
                            {post.imageUrl && (
                                <img
                                    src={`http://localhost:8080${post.imageUrl}`}
                                    alt="Post"
                                    style={{maxWidth: '200px', marginTop: '10px'}}
                                />
                            )}
                            <h3>{post.title}</h3>
                            <p>{post.content}</p>
                            <button onClick={() => handleDeletePost(post.id)}
                                    style={{backgroundColor: "red", color: "white"}}>
                                Delete Post
                            </button>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default UserPostsPage;
