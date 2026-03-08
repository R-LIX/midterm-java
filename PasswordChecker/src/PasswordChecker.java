import exception.InvalidPasswordException;
import model.Password;
import logic.PasswordScore;
import java.util.Scanner;

public class PasswordChecker {
    
    //TO-DO
    static void main() {
        // User input for username and password
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your username:");
        String username = scanner.nextLine();
        System.out.print("Please enter your password:");
        String password = scanner.nextLine();
        // Validate username and password
        System.out.print("Confirm set username & password:");
        String confirm = scanner.nextLine();
        if (confirm.equals("yes")){
        System.out.print("username & password Confirm (yes/no): ");
        }else{
            System.out.println("Please input again.");
            main();
        }
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
            System.out.println(e.getMessage());
        }
        if(password.length() < 8){
            System.out.println("Password must be at least 8 characters long.");
            main();
        }
        if (password.contains(username)){
            System.out.println("Password cannot contain the username!");
            main();
        }

}
    

    public String classifyPassword(int score) {

        return "im here";
    }

}
