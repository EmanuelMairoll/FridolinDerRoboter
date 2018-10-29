package com.emanuel.fridolin.exception;

public class InvalidCommandUsageException extends InvalidUserInputException {

    private static final long serialVersionUID = 5154636755247848122L;

    public InvalidCommandUsageException() {
    }

    public InvalidCommandUsageException(String message) {
        super(message);
    }

    public boolean isSpecified(){
        return getMessage() != null;
    }
}
