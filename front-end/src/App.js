import React from "react";
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import './App.css';
import HomePage from "./Components/HomePage";
import LoginPage from "./Components/LoginPage";
import CreatePostPage from "./Components/CreatePostPage";
import UserProfile from "./Components/UserProfile";

function App() {
    return (
        <Router>
            <div>
                <Routes>
                    <Route path="/" element={<HomePage/>}/>

                    <Route path={"/login"} element={<LoginPage/>}/>

                    <Route path={"/post/create"} element={<CreatePostPage/>}/>

                    <Route path={"/profile/:username"} element={<UserProfile/>}/>
                </Routes>
            </div>
        </Router>
    );
}

export default App;
