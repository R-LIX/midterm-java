import java.util.Scanner;

public class Main {  // Class names should start with uppercase

    public static void main(String[] args) {
        // Fei and Lika
        // Input and output recall
        // I want to add something here to test the commit and push function of git

        Scanner input = new Scanner(System.in);

        String username;
        String password;

        while (true) {
            System.out.print("Enter username: ");
            username = input.nextLine().toLowerCase();

            System.out.print("Enter password: ");
            password = input.nextLine();

            if (containsUsernamePart(username, password.toLowerCase())) {
                System.out.println("Password must not contain parts of the username. Try again\n");
            } else {
                break;
            }
        }

        int score = 0;

        // Lix and Miy - Points calculator
        if (password.length() >= 8) {
            score += 10;
        }

        if (password.matches(".*[!@#$%^&*(),.?\":{}|<>/\\\\\\[\\]_+=~`;-].*")) {
            score += 10;
        }

        if (!password.equals(password.toLowerCase())) {
            score += 10;
        }

        // Output password strength
        if (score < 15) {
            System.out.println("Your password is weak.");
        } else if (score == 15) {
            System.out.println("Your password is medium.");
        } else {
            System.out.println("Your password is strong.");
        }

        input.close();
    }

    // Hour and Yuth - Password validation helper
    public static boolean containsUsernamePart(String username, String password) {
        for (int i = 0; i < username.length(); i++) {
            for (int j = i + 2; j <= username.length(); j++) {
                String part = username.substring(i, j);
                if (password.contains(part)) {
                    return true;
                }
            }
        }
        return false;
    }

            //Hour and Yuth
        //Password Validation
 
        


        //Lix and miy
        //Points calculator and output
      
       
}