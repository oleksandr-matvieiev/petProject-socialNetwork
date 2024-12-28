package org.petproject.socialnetwork.Exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageNotFound  extends RuntimeException{
    private static final String DEFAULT_MESSAGE = ErrorMessages.MESSAGE_NOT_FOUND.getMessage();

    public MessageNotFound() {
        super(DEFAULT_MESSAGE);
        logError(DEFAULT_MESSAGE);
    }

    public MessageNotFound(String message) {
        super(message);
        logError(message);
    }

    private void logError(String message) {
        Logger logger = LoggerFactory.getLogger(EmailNotFound.class);
        logger.error(message);
    }

}
