
package logic;
package model;
import java.util.Scanner;


public class PasswordChecker {
    Scanner scanner = new Scanner(System.in);
    //TO-DO
    static void main() {
        // User input for username and password
        // Validate username and password
        // re-prompt user if there is an invalid input
        // Calculate score
        // Output password strength
        try {
            Password password = new Password("hour", "1");
        } catch (InvalidPasswordException e) {
            System.out.println(e.getMessage());;
        }

    }

    public String classifyPassword(int score) {

        return "im here";
    }

}

public class Password {
    private String username;
    private String password;

    public Password(String username, String password) throws InvalidPasswordException {

        this.username = username;
        // Validation logic here and throw exception.InvalidPasswordException if invalid
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
        return "model.Password{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}

public class PasswordScore {
    Password password;

    // TO DO
    public static int calculateScore() {
        // Return score for pattern and unique and more
        return 1;
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

