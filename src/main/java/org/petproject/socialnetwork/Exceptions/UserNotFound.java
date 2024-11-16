package org.petproject.socialnetwork.Exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserNotFound extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "User not found";

    public UserNotFound() {
        super(DEFAULT_MESSAGE);
        logError(DEFAULT_MESSAGE);
    }

    public UserNotFound(String message) {
        super(message);
        logError(message);
    }

    private void logError(String message) {
        Logger logger = LoggerFactory.getLogger(UserNotFound.class);
        logger.error(message);
    }
}
