package org.petproject.socialnetwork.Exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostNotFound extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Post not found.";

    public PostNotFound() {
        super(DEFAULT_MESSAGE);
        logError("Post not found.");
    }

    public PostNotFound(String message) {
        super(message);
        logError(message);
    }

    private void logError(String message) {
        Logger logger = LoggerFactory.getLogger(PostNotFound.class);
        logger.error(message);
    }
}
