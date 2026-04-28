package com.form.password;

public class Password {
    private String username;
    private String password;

    public Password(String username, String password) throws InvalidPasswordException {

        this.username = username;
        // Validation main.java.com.midterm_java.logic here and throw main.java.com.midterm_java.exception.InvalidPasswordException if invalid
        if (password.length() < 8) {
            throw new InvalidPasswordException("Password must be at least 8 characters long");
        }

        if (password.toLowerCase().contains(username.toLowerCase())) {
            throw new InvalidPasswordException("Password mustn't contain username");
        }

        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "main.java.com.midterm_java.model.Password{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
