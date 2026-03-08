package logic;

import model.Password;

public class PasswordScore {
    Password password;

    // TO DO
    public PasswordScore(Password password) {
        this.password = password;
    }

    public static int calculateScore(Password password) {
        // Return score for pattern and unique and more
        int score = 0;
        String pw = password.getPassword();

        score += specialSymbolScore(pw);
        score += numberScore(pw);
        score += capitalScore(pw);

        return score;
    }

    // +20 if password contains a special symbol
    private static int specialSymbolScore(String pw) {
        if (pw.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~].*")) {
            return 20;
        }
        return 0;
    }

    // +20 if password contains a number
    private static int numberScore(String pw) {
        if (pw.matches(".*[0-9].*")) {
            return 20;
        }
        return 0;
    }

    // +20 if password contains a capitalized letter
    private static int capitalScore(String pw) {
        if (pw.matches(".*[A-Z].*")) {
            return 20;
        }
        return 0;
    }

    // TO DO
    private int patternScore() {
        return 1;
    }

    // TO DO
    private int uniqueScore() {
        return 1;
    }

    // TO DO: more functions
}
