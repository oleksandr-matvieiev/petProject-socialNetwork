import React from "react";
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import './App.css';
import HomePage from "./Components/HomePage";
import LoginPage from "./Components/LoginPage";
import CreatePostPage from "./Components/CreatePostPage";
import UserProfile from "./Components/UserProfile";
import MessagesPage from "./Components/MessagesPage";
import VerifyEmailPage from "./Components/VerifyEmailPage";
import EditProfilePage from "./Components/EditProfilePage";
import NotificationsPage from "./Components/NotificationsPage";

function App() {
    return (
        <Router>
            <div>
                <Routes>
                    <Route path="/" element={<HomePage/>}/>

                    <Route path={"/login"} element={<LoginPage/>}/>

                    <Route path={"/post/create"} element={<CreatePostPage/>}/>

                    <Route path={"/profile/:username"} element={<UserProfile/>}/>

                    <Route path={"/messages"} element={<MessagesPage/>}/>

                    <Route path={"/verify-email"} element={<VerifyEmailPage/>}/>

                    <Route path={"/profile/edit"} element={<EditProfilePage/>}/>

                    <Route path={"/notifications"} element={<NotificationsPage/>}/>
                </Routes>
            </div>
        </Router>
    );
}

export default App;
