package com.form.password;
import java.math.BigInteger;
import java.security.MessageDigest;

public class Password {
    private static String hashingAlgorithm;

    public static boolean validate(String username, String password) throws InvalidPasswordException {
        if (password.length() < 8) {
            throw new InvalidPasswordException("Password must be at least 8 characters long");
        }

        if (password.toLowerCase().contains(username.toLowerCase())) {
            throw new InvalidPasswordException("Password mustn't contain username");
        }
        return true;
    }

     public static String hash(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance(hashingAlgorithm);
        byte[] messageDigest = md.digest(password.getBytes());
        // Convert to hex string
        return new BigInteger(1, messageDigest).toString(16);
    }

    public static Integer score(String password) {
        PasswordScore passwordScore = new PasswordScore(password);
        return passwordScore.getScore();
    }

    public static String rank(int score) {
        if (score <= 33) {
            return "Weak";
        } else if (score <= 66) {
            return "Medium";
        } else {
            return "Strong";
        }
    }


}
