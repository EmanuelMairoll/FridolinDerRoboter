package com.emanuel.fridolin.exception;

public class NoSuchColorException extends InvalidUserInputException {

    private final String colorName;

    public NoSuchColorException(String colorName) {
        this.colorName = colorName;
    }

    @Override
    public String getMessage() {
        return "No such Property \"" + colorName + "\"";
    }
}