package com.emanuel.fridolin.exception;

public class NoSuchPropertyException extends InvalidUserInputException {

    private final String propertyName;

    public NoSuchPropertyException(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String getMessage() {
        return "No such Property \"" + propertyName + "\"";
    }
}