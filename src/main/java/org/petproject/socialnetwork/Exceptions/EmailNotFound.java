package org.petproject.socialnetwork.Exceptions;

import org.petproject.socialnetwork.Enums.ErrorMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailNotFound extends RuntimeException {
    private static final String DEFAULT_MESSAGE = ErrorMessages.EMAIL_NOT_FOUND.getMessage();

    public EmailNotFound() {
        super(DEFAULT_MESSAGE);
        logError(DEFAULT_MESSAGE);
    }

    public EmailNotFound(String message) {
        super(message);
        logError(message);
    }

    private void logError(String message) {
        Logger logger = LoggerFactory.getLogger(EmailNotFound.class);
        logger.error(message);
    }
}
