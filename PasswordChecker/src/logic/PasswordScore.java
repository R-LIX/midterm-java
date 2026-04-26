package logic;

import model.Password;

public class PasswordScore {
    // Introduced class variable for password and score for easy access and remove redundancy in code
    private final String password;

    // TO DO
    public PasswordScore(Password password) {
        this.password = password.getPassword();
    }

    public int getScore() {
        // Return score for pattern and unique and more
        int score = 0;
        score += specialSymbolScore(password);
        score += numberScore(password);
        score += capitalScore(password);
        score += patternScore(password);
        score += uniqueScore(password);

        return score;
    }

    public static int calculateScore(String rawPassword) {
        int score = 0;
        score += specialSymbolScore(rawPassword);
        score += numberScore(rawPassword);
        score += capitalScore(rawPassword);
        score += patternScore(rawPassword);
        score += uniqueScore(rawPassword);
        return score;
    }

    public static String levelFromScore(int score) {
        if (score < 40) {
            return "Weak";
        }
        if (score <= 70) {
            return "Medium";
        }
        return "Strong";
    }

    public static int coreCriteriaCount(String rawPassword) {
        int count = 0;
        if (rawPassword.length() >= 8) {
            count++;
        }
        if (rawPassword.matches(".*[A-Z].*")) {
            count++;
        }
        if (rawPassword.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~].*")) {
            count++;
        }
        if (rawPassword.matches(".*[0-9].*")) {
            count++;
        }
        return count;
    }

    public static String levelFromCriteriaCount(int count) {
        if (count == 4) {
            return "Strong";
        }
        if (count >= 2) {
            return "Medium";
        }
        return "Weak";
    }

    // +20 if password contains a special symbol
    private static int specialSymbolScore(String pwd) {
        if (pwd.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~].*")) {
            return 20;
        }
        return 0;
    }

    // +20 if password contains a number
    private static int numberScore(String pwd) {
        if (pwd.matches(".*[0-9].*")) {
            return 20;
        }
        return 0;
    }

    // +20 if password contains a capitalized letter
    private static int capitalScore(String pwd) {
        if (pwd.matches(".*[A-Z].*")) {
            return 20;
        }
        return 0;
    }

    // TO DO
    private static int patternScore(String pwd) {
        if (pwd.matches(".*(12345678|abcd1234|password|qwerty12|letmein12|admin1234|welcome12|iloveyou1|pass1234|qwerty123|abc12345|abc123456|asdf1234|asdfghjk|zxcvbnm1|zxcvbnm12|qazwsxed|qazwsxed12|test1234|test12345|user12345|login1234|guest1234|root12345|master12|dragon123|shadow123|monkey123|football1|sunshine1|princess1|superman1|freedom12|whatever1|trustno1|hunter22|killer12|batman12|pokemon12|naruto123|welcome123).*")) {
            return -38;
        }
        return 20;
    }

    // TO DO
    private static int uniqueScore(String pwd) {
        if (pwd.length() <= 1) {
            return 20;
        }

        int repeatedAdjacent = 0;
        for (int i = 0; i < pwd.length() - 1; i++) {
            if (pwd.charAt(i) == pwd.charAt(i + 1)) {
                repeatedAdjacent++;
            }
        }

        int penalty = repeatedAdjacent * (20 / pwd.length());
        int score = 20 - penalty;
        return Math.max(0, score);
    }

    // TO DO: more functions
}
