package org.petproject.socialnetwork.Exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoleNotFound extends RuntimeException {
    public static final String DEFAULT_MESSAGE = ErrorMessages.ROLE_NOT_FOUND.getMessage();

    public RoleNotFound() {
        super(DEFAULT_MESSAGE);
        logError(DEFAULT_MESSAGE);
    }

    public RoleNotFound(String message) {
        super(message);
        logError(message);
    }


    public void logError(String message) {
        Logger logger = LoggerFactory.getLogger(RoleNotFound.class);
        logger.error(message);
    }

}
