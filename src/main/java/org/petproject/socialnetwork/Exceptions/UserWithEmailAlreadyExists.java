package org.petproject.socialnetwork.Exceptions;

import org.petproject.socialnetwork.Enums.ErrorMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserWithEmailAlreadyExists extends RuntimeException {
    private static final String DEFAULT_MESSAGE= ErrorMessages.EMAIL_ALREADY_REGISTERED.getMessage();
    public UserWithEmailAlreadyExists(String message) {
        super(message);
        logError(message);
    }
    public UserWithEmailAlreadyExists(){
        super(DEFAULT_MESSAGE);
        logError(DEFAULT_MESSAGE);
    }
    public void logError(String message){
        Logger logger= LoggerFactory.getLogger(UserWithEmailAlreadyExists.class);
        logger.error(message);
    }
}
