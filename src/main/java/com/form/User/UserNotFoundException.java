package com.form.User;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super("User Doesn't exist: " +  message);
    }
}
