package com.url.urlmanager.domain.exception;

public class DuplicateShortUrlException extends ShortUrlGenerationException {

    public DuplicateShortUrlException(String message) {
        super(message);
    }

    public DuplicateShortUrlException(String message, Throwable cause) {
        super(message, cause);
    }
}
