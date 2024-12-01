import {useState} from 'react';
import axios from "axios";

const CreatePostPage = () => {
    const [posts, setPosts] = useState([]);
    const [content, setContent] = useState("");
    const [imageUrl, setImageUrl] = useState("");
    const apiBaseUrl = 'http://localhost:8080/api/posts';

    const handleCreatePost = async (e) => {
        e.preventDefault();
        try {
            const token=localStorage.getItem("Token");
            const response = await axios.post(`${apiBaseUrl}/createPost`, {content,imageUrl}, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            setPosts([response.data, ...posts]);
            setContent("");
            setImageUrl("");

            console.log('Post created');
        } catch (err) {
            console.error('Error creating post:', err);
        }
    };

    return(
        <div>
            <h2>Create Post</h2>
            <textarea
                value={content}
                onChange={(e) => setContent(e.target.value)}
                placeholder="Write your post..."
            />
            <input
                type="text"
                value={imageUrl}
                onChange={(e) => setImageUrl(e.target.value)}
                placeholder="Image URL"
            />
            <button onClick={handleCreatePost}>Create Post</button>
        </div>
    )
}
export default CreatePostPage;