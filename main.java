import java.util.Scanner;
public class main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        String username;
        String password;

        while (true) {
            System.out.print("Enter username: ");
            username = input.nextLine().toLowerCase();

            System.out.print("Enter password: ");
            password = input.nextLine().toLowerCase();

            if (containsUsernamePart(username, password)) {
                System.out.println("Password must not contain parts of the username. Try again\n");
            }
            else {
                break;
            }
        }

        int score = 0;
        if (password.length() >=8) {
            score +=10;
        }
        if (password.matches(".*[!@#$%^&*(),.?\":{}|<>/\\\\\\[\\]_+=~`;-].*")) {
            score +=10;
        }
        if (!password.equals(password.toLowerCase())) {
            score +=10;
        }

        if (score < 15) {
            System.out.println("Your password is weak.");
        } else if (score == 15) {
            System.out.println("Your password is medium.");
        } else if (score > 15) {
            System.out.println("Your password is strong.");
        }
    }

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
        //Fei and Lika
        //Input and output recall
        //I want to add smth here to test the commit and push function of git
        
        
        
        //Hour and Yuth
        //Password Validation
 
        


        //Lix and miy
        //Points calculator and output
      
       
        }
    }
