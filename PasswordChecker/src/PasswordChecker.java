import exception.InvalidPasswordException;
import model.Password;
import logic.PasswordScore;
import java.util.Scanner;


public class PasswordChecker {
    
    //TO-DO
    public static void main(String[] args) {
        // User input for username and password
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your username:");
        String username = scanner.nextLine();
        System.out.print("Please enter your password:");
        String password = scanner.nextLine();
        // Validate username and password
        System.out.print("Confirm set username & password (yes/no):");
        String confirm = scanner.nextLine();
        if (confirm.equals("yes")){
        System.out.println("username & password Confirm! ");
        }else{
            System.out.println("Please input again.");
            main(args);
        }
        // re-prompt user if there is an invalid input
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Invalid input. Please try again.");
            main(args);
        }
        // Calculate score
    
        // Output password strength
        Password passwordObj = null;
        try {
            passwordObj = new Password(username, password);
        } catch (InvalidPasswordException e) {
            System.out.println(e.getMessage());
        }
        if(password.length() < 8){
            System.out.println("Password must be at least 8 characters long.");
            main(args);
        }
        if (password.contains(username)){
            System.out.println("Password cannot contain the username!");
            main(args);
        }
        int score = PasswordScore.calculateScore(passwordObj);
        String strength = classifyPassword(score);
        if(score > 0) {
            System.out.println("Password Score: " + score);
        
        System.out.println("Password Strength: " + strength);
        System.out.println("Password is valid.");
    }

}
    

    public static String classifyPassword(int score) {
        if (score <= 33) {
            return "Weak";
        } else if (score <= 66 && score>33) {
            return "Medium";
        } else {
            return "Strong";
        }
    }

}