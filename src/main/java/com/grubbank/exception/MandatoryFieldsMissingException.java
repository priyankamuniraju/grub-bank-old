package com.grubbank.exception;

public class MandatoryFieldsMissingException extends RuntimeException{
    private String message;

    public MandatoryFieldsMissingException(String message) {
        super(message);
        this.message = message;
    }
    public MandatoryFieldsMissingException() {
    }
}
