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
        if (pw.matches(".*(12345678|abcd1234|password|qwerty12|letmein12|admin1234|welcome12|iloveyou1|pass1234|qwerty123|abc12345|abc123456|asdf1234|asdfghjk|zxcvbnm1|zxcvbnm12|qazwsxed|qazwsxed12|test1234|test12345|user12345|login1234|guest1234|root12345|master12|dragon123|shadow123|monkey123|football1|sunshine1|princess1|superman1|freedom12|whatever1|trustno1|hunter22|killer12|batman12|pokemon12|naruto123|welcome123).*")) {
            return -38;
        }
        return 20;
    }

    // TO DO
    private static int uniqueScore(String pw) {
        String[] stringArray = pw.split("");
        for (int i=0; i<stringArray.length -1; i++){
            if(stringArray[i].contains(stringArray[i+1])) {
                int count = 0;
                count++;
                return 20-count*(20/stringArray.length);
            }
        }
        return 20;
    }

    // TO DO: more functions
}
