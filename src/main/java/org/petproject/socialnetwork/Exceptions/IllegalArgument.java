package org.petproject.socialnetwork.Exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IllegalArgument extends RuntimeException {
    private static final String DEFAULT_MESSAGE = ErrorMessages.ILLEGAL_ARGUMENT.getMessage();

    public IllegalArgument() {
        super(DEFAULT_MESSAGE);
        logError(DEFAULT_MESSAGE);
    }

    public IllegalArgument(String message) {
        super(message);
        logError(message);
    }

    public void logError(String message) {
        Logger logger = LoggerFactory.getLogger(IllegalArgument.class);
        logger.error(message);
    }
}
