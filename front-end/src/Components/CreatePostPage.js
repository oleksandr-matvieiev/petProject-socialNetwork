import {useState} from 'react';
import axios from "axios";

const CreatePostPage = () => {
    const [posts, setPosts] = useState([]);
    const [content, setContent] = useState("");
    const [file, setFile] = useState(null);
    const apiBaseUrl = 'http://localhost:8080/api/posts';

    const handleCreatePost = async (e) => {
        e.preventDefault();
        try {
            const token = localStorage.getItem("Token");
            if (!token) {
                console.error('No token found in localStorage');
                return;
            }
            const formData = new FormData();
            formData.append("content", content);
            if (file) {
                formData.append("image", file);
            }

            const response = await axios.post(`${apiBaseUrl}/createPost`, formData, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'multipart/form-data'
                },
            });
            setPosts([response.data, ...posts]);
            setContent("");
            setFile(null);

            console.log('Post created');
        } catch (err) {
            console.error('Error creating post:', err);
        }
    };

    return (
        <div>
            <h2>Create Post</h2>
            <form onSubmit={handleCreatePost}>
                <textarea
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                    placeholder="Write your post..."
                />
                <input
                    type="file"
                    onChange={(e) => setFile(e.target.files[0])} // Обробка вибору файлу
                />
                <button type="submit">Create Post</button>
            </form>
            <div>
                <h3>Posts</h3>
                {posts.map((post) => (
                    <div key={post.id}>
                        <p>{post.content}</p>
                        {post.imageUrl && <img src={post.imageUrl} alt="Post" style={{maxWidth: "300px"}}/>}
                    </div>
                ))}
            </div>
        </div>
    );
};

export default CreatePostPage;