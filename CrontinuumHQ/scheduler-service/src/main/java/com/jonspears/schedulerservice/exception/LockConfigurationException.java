package com.jonspears.schedulerservice.exception;

public class LockConfigurationException extends RuntimeException {
    public LockConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
