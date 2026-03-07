import exception.InvalidPasswordException;
import model.Password;
import logic.PasswordScore;
import java.util.Scanner;

public class PasswordChecker {
    
    //TO-DO
    static void main() {
        // User input for username and password
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your username:");
        String username = scanner.nextLine();
        System.out.println("Please enter your password:");
        String password = scanner.nextLine();
        // Validate username and password
        
        // re-prompt user if there is an invalid input
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Invalid input. Please try again.");
            main();
        }
        // Calculate score
    
        // Output password strength
        try {
            Password passwordObj = new Password("hour", "1");
        } catch (InvalidPasswordException e) {
            System.out.println(e.getMessage());;
        }

    }

    public String classifyPassword(int score) {

        return "im here";
    }

}
