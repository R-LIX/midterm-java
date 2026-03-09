import exception.InvalidPasswordException;
import model.Password;
import logic.PasswordScore;
import java.util.Scanner;


public class PasswordChecker {
    private final Scanner scanner = new Scanner(System.in);

    //TO-DO
    public void main(String[] args) {

        // User input for username and password

        Password password = inputPassword();
        if (password == null) {
            System.out.println("Goodbye");
            return;
        }

//        // Calculate score
//
//        // Output password strength


//
//        int score = PasswordScore.calculateScore(passwordObj);
//        String strength = classifyPassword(score);
//
//        if (score > 0) {
//            System.out.println("Password Score: " + score);
//
//            System.out.println("Password Strength: " + strength);
//            System.out.println("Want to try again? (yes/no):");
//        }
//
//        if (scanner.nextLine().equals("yes")) {
//            main(args);
//        } else {
//            System.out.println("Goodbye!");
//        }
//    }
//
//
//

    }
//    public static String classifyPassword(int score) {
//        if (score <= 33) {
//            return "Weak";
//        } else if (score <= 66 && score > 33) {
//            return "Medium";
//        } else {
//            return "Strong";
//        }
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

}

