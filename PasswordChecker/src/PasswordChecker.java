import exception.InvalidPasswordException;
import model.Password;

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
