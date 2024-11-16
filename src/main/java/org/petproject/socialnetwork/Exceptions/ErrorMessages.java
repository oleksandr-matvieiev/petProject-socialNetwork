package org.petproject.socialnetwork.Exceptions;

public enum ErrorMessages {
    USER_NOT_FOUND("User not found."),
    EMAIL_NOT_FOUND("Email not found."),
    USERNAME_ALREADY_EXISTS("Username is already taken."),
    EMAIL_ALREADY_REGISTERED("Email is already registered."),
    POST_NOT_FOUND("Post not found."),
    FOLLOW_SELF("You cannot follow yourself."),
    FOLLOW_ALREADY_EXISTS("Already following."),
    POST_DOES_NOT_EXIST("Post does not exist.");


    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
