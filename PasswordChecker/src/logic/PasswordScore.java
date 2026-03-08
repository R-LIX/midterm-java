package logic;
import java.util.List;
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
        score += patternScore(pw);
        score += uniqueScore(pw);

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
    private static int patternScore(String pw) {
        if (pw.matches(".*(12345678|abcd1234|password|qwerty12|letmein|admin|welcome|iloveyou).*")) {
            return 0;
        }
        return 20;
    }

    // TO DO
    private static int uniqueScore(String pw) {
        String[] stringArray = pw.split("");
        for (int i=0; i<stringArray.length -1; i++){
            if(stringArray[i].contains(stringArray[i+1])) {
                return 0;
            }
        }
        return 1;
    }

    // TO DO: more functions
}
