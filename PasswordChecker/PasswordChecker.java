import exception.InvalidPasswordException;
import model.Password;
import logic.PasswordScore;
import java.util.Scanner;


public class PasswordChecker {
    private final Scanner scanner = new Scanner(System.in);

    public void main(String[] args) {
        // Boolean flag to keeps program running until user says ends
        boolean running = true;
        // User input for username and password
        while (running) {
            Password password = inputPassword();
            if (password == null) {
                System.out.println("Goodbye");
                return;
            }
            // Calculate score
            PasswordScore passwordScore = new PasswordScore(password);
            int score = passwordScore.getScore();
            // Output password strength
            outputPasswordStrength(score);
            // Ask user to try again
            running = tryAgain();
        }
    }

    public Password inputPassword() {
        while (true) {
            System.out.print("Please enter your username: ");
            String username = scanner.nextLine();

            System.out.print("Please enter your password: ");
            String password = scanner.nextLine();

            // Validate username and password
            System.out.printf("""
                         
                         Press 1 to confirm
                         Press 2 to start over
                         Press any key to abort
                         ------------------------------------
                         Username: %s
                         Password: %s
                         ------------------------------------
                         """ , username, password);
            String userInput= scanner.nextLine();
            if (userInput.equals("1")) {
                try {
                    return new Password(username, password);
                } catch (InvalidPasswordException ipe) {
                    System.out.println(ipe.getMessage());
                }

            } else if (userInput.equals("2")) {
                return inputPassword();

            } else {
                return null;
            }

        }
    }
    public void outputPasswordStrength(int score) {
        String strength = classifyPassword(score);
        if (score > 0) {
            System.out.println("Password Score: " + score);
        }
            System.out.println("Password Strength: " + strength);

    }
    public boolean tryAgain() {
        System.out.println("Want to try again? (y/n):");
        String userInput = scanner.nextLine();

        if (userInput.equalsIgnoreCase("n")) {
            System.out.println("Goodbye");
            return false;
        }
        return true;
    }

    // Helper function
    public static String classifyPassword(int score) {
        if (score <= 33) {
            return "Weak";
        } else if (score <= 66 && score > 33) {
            return "Medium";
        } else {
            return "Strong";
        }
    }

}

