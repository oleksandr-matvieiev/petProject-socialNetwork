package org.petproject.socialnetwork.Exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAlreadyExists extends RuntimeException {
    private static final String DEFAULT_MESSAGE = ErrorMessages.USERNAME_ALREADY_EXISTS.getMessage();

    public UserAlreadyExists() {
        super(DEFAULT_MESSAGE);
        logError(DEFAULT_MESSAGE);
    }

    public UserAlreadyExists(String message) {
        super(message);
        logError(message);
    }

    private void logError(String message) {
        Logger logger = LoggerFactory.getLogger(UserAlreadyExists.class);
        logger.error(message);
    }
}
