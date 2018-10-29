package com.emanuel.fridolin.exception;

@SuppressWarnings("WeakerAccess")
public class InvalidUserInputException extends Exception {

    public InvalidUserInputException() {
    }

    public InvalidUserInputException(String message) {
        super(message);
    }

    public InvalidUserInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUserInputException(Throwable cause) {
        super(cause);
    }

    public InvalidUserInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
