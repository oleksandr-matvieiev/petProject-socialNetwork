import React from "react";
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import './App.css';
import HomePage from "./Components/Posts/HomePage";
import LoginPage from "./Components/Auth/LoginPage";
import CreatePostPage from "./Components/Posts/CreatePostPage";
import UserProfile from "./Components/Users/UserProfile";
import MessagesPage from "./Components/Messages/MessagesPage";
import VerifyEmailPage from "./Components/Auth/VerifyEmailPage";
import EditProfilePage from "./Components/Users/EditProfilePage";
import NotificationsPage from "./Components/Users/NotificationsPage";
import AdminDashboardPage from "./Components/Admin/AdminDashboardPage";
import SendEmailPage from "./Components/Admin/SendEmailPage";
import UnauthorizedPage from "./Components/Auth/UnauthorizedPage";
import UsersListPage from "./Components/Admin/UsersListPage";
import AdminActionsPage from "./Components/Admin/AdminActionsPage";
import UserPostsPage from "./Components/Admin/UserPostsPage";

function App() {
    return (
        <Router>
            <div>
                <Routes>
                    <Route path="/" element={<HomePage/>}/>

                    <Route path={"/login"} element={<LoginPage/>}/>

                    <Route path={"/post/create"} element={<CreatePostPage/>}/>

                    <Route path={"/profile/:username"} element={<UserProfile/>}/>

                    <Route path={"/profile/edit"} element={<EditProfilePage/>}/>

                    <Route path={"/messages"} element={<MessagesPage/>}/>

                    <Route path={"/verify-email"} element={<VerifyEmailPage/>}/>

                    <Route path={"/notifications"} element={<NotificationsPage/>}/>

                    <Route path={"/admin/dashboard"} element={<AdminDashboardPage/>}/>

                    <Route path={"/admin/send-email"} element={<SendEmailPage/>}/>

                    <Route path="/unauthorized" element={<UnauthorizedPage/>}/>

                    <Route path="/admin/users" element={<UsersListPage/>}/>

                    <Route path="/admin/actions/:username" element={<AdminActionsPage/>}/>

                    <Route path="/admin/actions/posts/:username" element={<UserPostsPage/>}/>
                </Routes>
            </div>
        </Router>
    );
}

export default App;
