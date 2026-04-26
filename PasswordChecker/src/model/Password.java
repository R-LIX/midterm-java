package model;

import exception.InvalidPasswordException;
import java.security.MessageDigest;
import java.math.BigInteger;

public class Password {
    private String username;
    private String password;

    public Password(String username, String password) throws InvalidPasswordException {

        this.username = username;
        // Validation logic here and throw exception.InvalidPasswordException if invalid
        if (password.length() < 8) {
            throw new InvalidPasswordException("Password must be at least 8 characters long");
        }

        if (password.toLowerCase().contains(username.toLowerCase())) {
            throw new InvalidPasswordException("Password mustn't contain username");
        }

        this.password = password;
    }

    public  String getPassword() {
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
public static String getMD5(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        // Convert to hex string
        return new BigInteger(1, messageDigest).toString(16);
    }
    @Override
    public String toString() {
        return "model.Password{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
