package logic;

import model.Password;

public class PasswordScore {
    // Introduced class variable for password and score for easy access and remove redundancy in code
    private final String password;
    private int score = 0;

    // TO DO
    public PasswordScore(Password password) {
        this.password = password.getPassword();
    }

    public int getScore() {
        // Return score for pattern and unique and more
        score += specialSymbolScore();
        score += numberScore();
        score += capitalScore();
        score += patternScore();
        score += uniqueScore();

        return score;
    }

    // +20 if password contains a special symbol
    private int specialSymbolScore() {
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~].*")) {
            return 20;
        }
        return 0;
    }

    // +20 if password contains a number
    private int numberScore() {
        if (password.matches(".*[0-9].*")) {
            return 20;
        }
        return 0;
    }

    // +20 if password contains a capitalized letter
    private int capitalScore() {
        if (password.matches(".*[A-Z].*")) {
            return 20;
        }
        return 0;
    }

    // TO DO
    private  int patternScore() {
        if (password.matches(".*(12345678|abcd1234|password|qwerty12|letmein12|admin1234|welcome12|iloveyou1|pass1234|qwerty123|abc12345|abc123456|asdf1234|asdfghjk|zxcvbnm1|zxcvbnm12|qazwsxed|qazwsxed12|test1234|test12345|user12345|login1234|guest1234|root12345|master12|dragon123|shadow123|monkey123|football1|sunshine1|princess1|superman1|freedom12|whatever1|trustno1|hunter22|killer12|batman12|pokemon12|naruto123|welcome123).*")) {
            return -38;
        }
        return 20;
    }

    // TO DO
    private int uniqueScore() {
        String[] stringArray = password.split("");
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
