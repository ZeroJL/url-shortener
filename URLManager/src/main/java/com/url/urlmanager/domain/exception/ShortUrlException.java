package com.url.urlmanager.domain.exception;

public class ShortUrlException extends RuntimeException {
    public ShortUrlException(String message) {
        super(message);
    }

    public ShortUrlException(String message, Throwable cause) {
        super(message, cause);
    }
}
