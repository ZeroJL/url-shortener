package com.clone.urlshortener.codec.exception;

public class UnknownStrategyException extends RuntimeException {
    public UnknownStrategyException(String message) {
        super(message);
    }
}
