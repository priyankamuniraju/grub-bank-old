package com.grubbank.exception;


public class RecipeNotFoundException extends RuntimeException{
    private String message;

    public RecipeNotFoundException(String message) {
        super(message);
        this.message = message;
    }
    public RecipeNotFoundException() {
    }
}
