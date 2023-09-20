package com.generator.keygenerator.domain.exception;

public class NoAvailableKeysException extends RuntimeException {
    public NoAvailableKeysException(String message) {
        super(message);
    }
}
