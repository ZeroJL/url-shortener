package com.url.urlmanager.domain.exception;

public class ShortUrlGenerationException extends ShortUrlException {

    public ShortUrlGenerationException(String message) {
        super(message);
    }

    public ShortUrlGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
