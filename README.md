# Social Network Project

## Overview

This project serves as the backend for a social network platform, developed with **Spring Boot**. It offers key
functionalities including role-based access, user registration and login, creating posts and interaction wit them.

## Features

- **User Management**: Register and verify email through a 6-digit code, log in securely.
- **User profile Management**: Update users profile, including bio and profile picture.
- **Post Management**: Creating posts,write comments and likes for them.
- **JWT-based Security**: Role-based access control with JWT for authentication.
- **Image Upload**: Ability to upload and attach posts and user profile images.
- **Messaging**: Ability to create chats with another users and send them messages.
- **Admin Management:** Manage users, posts, and roles via admin APIs.

## Technologies

- **Java 21** and **Spring Boot**
- **Spring Security** with JWT and role-based access control
- **Spring Data JPA** for data persistence
- **MySQL** as the database
- **Jakarta Mail** for email functionality
- **MultipartFile** for file uploads
- **JSON Web Token (JWT)** for securing the API authentication
- **Lombok** for simplifying Java classes
- **MapStruct** for object mapping between DTOs and entities.

## Requirements

- Java 21
- Maven 3+
- MySQL
- Postman (for testing API endpoints)
- Frontend client (optional, recommended React)

## Installation and Setup

### Step 1: Clone the Repository

```
git clone https://github.com/oleksandr-matvieiev/petProject-socialNetwork.git
cd petProject-socialNetwork
```

### Step 2: Configure Database

1.Edit ```application.properties``` under ```src/main/resources``` with your MySQL database credentials:

  ```
  spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
  spring.datasource.username=your_username
  spring.datasource.password=your_password
  ```

### Step 3: Set up Email for password reset

Configure email settings in ```application.properties``` to enable account verification via email:

```
  spring.mail.host=smtp.gmail.com
  spring.mail.port=587
  spring.mail.username=your-email@gmail.com
  spring.mail.password=your-email-app-password
  spring.mail.properties.mail.smtp.auth=true
  spring.mail.properties.mail.smtp.starttls.enable=true
```

### Step 4: Run the Application

Use Maven to build and run the project:

```
  mvn clean install
  mvn spring-boot:run
```

Use Terminal to run frontend part(Optional):

```
cd front-end
npm install
npm start
```

### Step 5: Access the api

- The API will be available at: http://localhost:8080
- Access to the front-end is available at: http://localhost:3000

## Usage

### Admin Management
The ```AdminController``` enables administrative features like managing users, posts, and roles.

1. **Send Email to All Users**
- Send an email to all registered users:  
```POST /api/admin/send-email-to-all```
- Request Parameters:

```
subject: Email subject
message: Email message
```

2. **Get All Users**
- Retrieve all users or search by username/email:  
```GET /api/admin/get-all-users```
- Request Parameters:
```
search (optional): {A keyword to filter users}
```
3. **View Account Info**
- Get detailed account info for a specific user:  
```GET /api/admin/actions/view-account-info/{username}```

4. **Delete Account**
- Delete a user's account by username:  
```DELETE /api/admin/actions/delete-account/{username}```

5. **Send Email to a User**
- Send an email to a specific user:  
```POST /api/admin/actions/send-email/{username}```
- Request Body (JSON):

```json
{
  "subject": "Your Subject",
  "content": "Your message content"
}
```
6. **Promote User**
- Promote a user to a specific role:  
```POST /api/admin/actions/promote/{username}```
- Request Parameters:
```
roleName: {Role to assign} (e.g., ADMIN).
```
7. **Demote User**
- Demote a user from a specific role:  
```POST /api/admin/actions/demote/{username}```
- Request Parameters:

```
roleName: {Role to remove} (e.g., ADMIN).
```
8. **View User Posts**
- Retrieve all posts created by a specific user:  
```GET /api/admin/actions/posts/{username}```

9. **Delete User Post**
- Delete a specific post by its ID:  
```DELETE /api/admin/actions/posts/{username}/{postId}```

### User Authentication

**1. User Registration**

- Register a new user by sending a ```POST``` request to:  
  ``` POST /api/auth/register ```
- Request body (form data):

```markdown
username=exampleUser
email=example@domain.com
password=strongPassword
bio=Short bio (optional)
image=profileImage.jpg (optional)
```

- Verify a user's email address:  
  ```POST /api/auth/verify-email```
- Request body (JSON):

```json
{
  "email": "example@domain.com",
  "code": "verification-code"
}
```

**2. User Login**

- Log in to retrieve a JWT token:  
  ```POST /api/auth/login```
- Request body (JSON):

```json
{
  "username": "exampleUser",
  "password": "strongPassword"
}
```

**3. Get Current User**
Retrieve the username of the logged-in user:  
```GET /api/auth/me```

### Chat Management

**1. Create a Chat**

- Create a chat with another user:   
  ```POST /api/chats/create```
- Request parameter:

```markdown
receiverUsername=otherUser
```

**2. Get User Chats**

- Retrieve all chats for the logged-in user:   
  ```GET /api/chats```

### Messaging

**1. Send Message**

- Send a message to a user:   
  ```POST /api/messages/send```
- Request body (JSON):

```json
{
  "receiver": "otherUser",
  "content": "Hello, how are you?"
}
```

**2. Get Conversation**

- Retrieve the conversation with a specific user:  
  ```GET /api/messages/conversation/{receiver}```

**3. Inbox Messages**

- Retrieve all received messages:  
  ```GET /api/messages/inbox```

**4. Mark Message as Read**

- Mark a specific message as read:   
  ```POST /api/messages/{messageId}/read```

### Post Management

**1. Create a Post**

- Create a new post:   
  ```POST /api/posts/createPost```
- Request body (form data):

```
content=Post content
image=postImage.jpg (optional)
```

**2. Get All Posts**

- Retrieve all posts:   
  ```GET /api/posts/allPosts```

**3. Get User's Posts**

- Retrieve all posts created by a specific user:   
  ```GET /api/posts/userPosts/{username}```

**4. Delete a Post**

- Delete a post by its ID:   
  ```DELETE /api/posts/deletePost/{postId}```

**5. Like/Unlike a Post**

- Toggle a like for a specific post:   
  ```POST /api/posts/{postId}/like```

**6.Add a Comment**

- Add a comment to a post:   
  ```POST /api/posts/{postId}/addComment```
- Request body (JSON):

```json
{
  "content": "Great post!"
}
```

**7.Get Comments for a Post**

- Retrieve all comments for a post:   
  ```GET /api/posts/{postId}/comments```

### User Management

**1.Get User Profile**

- Retrieve a user's profile by username:   
  ```GET /api/user/{username}```

**2.Get User's Posts**

- Retrieve all posts created by a specific user:  
  ```GET /api/user/{username}/posts```

**3. Edit Profile**

- Update bio or profile picture:  
  ```POST /api/user/edit/profile-info```
- Request body (form data):
```
bio=New bio (optional)
newPicture=newProfileImage.jpg (optional)
```
### Follow Management

**1. Follow user**

- Follow another user: ```POST /api/follow/{followeeUsername}```
 
**2. Unfollow User**
- Unfollow user you followed:
```DELETE /api/follow/{followeeUsername}```

**3. Get user's followers**
- Get list of user's followers:
```GET /api/follow/{username}/followers```

**4. Get user's following**
- Get list of user's following:
```GET /api/follow/{username}/following```

## API Security
- **JWT Authentication**: Secure all endpoints (except for login, registration) with JWT tokens.
- **Role-Based Access Control**:Ensure role-specific access
- **User**: Can browse posts, likes and comments them, make chats with another users and send them messages, edit profile...(in develop).
- **Admin**: Can send email to every user and to chosen user, get list of all users(also using search filter),view every users info, delete user's account, view and delete user posts...(in develop).
- **Super Admin**: All from admin, promote and demote user roles...(in develop).
