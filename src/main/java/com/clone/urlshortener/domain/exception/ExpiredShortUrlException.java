package com.clone.urlshortener.domain.exception;

public class ExpiredShortUrlException extends ShortUrlException {
    public ExpiredShortUrlException(String message) {
        super(message);
    }
}
