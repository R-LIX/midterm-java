package com.form.User;

public class DuplicateUserException extends RuntimeException{
    public DuplicateUserException(String message) {
        super("User already exist: " + message);
    }
}
