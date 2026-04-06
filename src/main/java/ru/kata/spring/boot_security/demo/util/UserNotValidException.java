package ru.kata.spring.boot_security.demo.util;

public class UserNotValidException extends RuntimeException {

    public UserNotValidException(String message) {
        super(message);
    }
}
