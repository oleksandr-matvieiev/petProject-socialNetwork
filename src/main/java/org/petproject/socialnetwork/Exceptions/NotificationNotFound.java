package org.petproject.socialnetwork.Exceptions;

import org.petproject.socialnetwork.Enums.ErrorMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationNotFound extends RuntimeException{

    private static final String DEFAULT_MESSAGE = ErrorMessages.NOTIFICATION_NOT_FOUND.getMessage();

    public NotificationNotFound() {
        super(DEFAULT_MESSAGE);
        logError(DEFAULT_MESSAGE);
    }

    public NotificationNotFound(String message) {
        super(message);
        logError(message);
    }

    private void logError(String message) {
        Logger logger = LoggerFactory.getLogger(EmailNotFound.class);
        logger.error(message);
    }
}
