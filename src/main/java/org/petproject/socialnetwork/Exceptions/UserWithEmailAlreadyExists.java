package org.petproject.socialnetwork.Exceptions;

public class UserWithEmailAlreadyExists extends RuntimeException{
    public UserWithEmailAlreadyExists(String message) {
    super(message);
    }
}
