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
## API Security
- **JWT Authentication**: Secure all endpoints (except for login, registration, and password reset) with JWT tokens.
- **Role-Based Access Control**:Ensure role-specific access
- **User**: Can browse posts, likes and comments them, make chats with another users and send them messages, edit profile...(in develop).
- **Admin**: In develop.
- **Super Admin**: In develop.
