package ru.kata.spring.boot_security.demo.util;

public class UserErrorResponse extends RuntimeException{
    private String message;

    public UserErrorResponse(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
