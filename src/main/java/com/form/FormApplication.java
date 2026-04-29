package com.form;

import com.form.password.InvalidPasswordException;
import com.form.password.Password;

import java.util.Scanner;

public class FormApplication {
    private final FormService formService = new FormService();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new FormApplication().run();
    }

    public void run() {
        while (true) {
            System.out.println("\n1. Sign In\n2. Sign Up\n3. Exit\nEnter your choice: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> handleSignIn();
                case "2" -> handleSignUp();
                case "3" -> { System.out.println("Goodbye!"); return; }
                default  -> System.out.println("Invalid choice.");
            }
        }
    }

    private void handleSignIn() {
        int tries = 3;
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        while (tries > 0) {
            if (!formService.usernameExists(username)) {
                System.out.println("User not found.");
                System.out.println("1. Try a different username\n2. Exit to main menu");
                if (scanner.nextLine().trim().equals("2")) return;
                System.out.print("Username: ");
                username = scanner.nextLine().trim();
                continue;
            }

            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            if (formService.signIn(username, password)) {
                System.out.println("Successfully signed in!");
                showLoggedInMenu();
                return;
            }

            tries--;
            if (tries == 0) { System.out.println("Too many failed attempts. Exiting."); System.exit(0); }

            System.out.println("Invalid credentials. " + tries + " tries remaining.");
            System.out.println("1. Try again\n2. Try a different username\n3. Exit to main menu");
            String choice = scanner.nextLine().trim();
            if (choice.equals("2")) {
                System.out.print("Username: ");
                username = scanner.nextLine().trim();
            } else if (choice.equals("3")) {
                return;
            }
        }
    }

    private void handleSignUp() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        if (formService.usernameExists(username)) {
            System.out.println("Username already taken.");
            return;
        }

        String password = "";

        while (true) {
            System.out.print("Password: ");
            password = scanner.nextLine().trim();

            try {
                Password.validate(username, password);
            } catch (InvalidPasswordException e) {
                System.out.println(e.getMessage());
                continue;
            }

            System.out.println("Password rank: " + Password.rank(password));
            System.out.print("Try a different password for a better rank? (y/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("n")) break;
        }

        if (formService.signUp(username, password)) {
            System.out.println("Successfully signed up!");
            showLoggedInMenu();
        } else {
            System.out.println("Username already taken.");
        }
    }

    private void showLoggedInMenu() {
        while (true) {
            System.out.println("\n1. Sign Out");
            System.out.println("Enter your choice:");
            if (scanner.nextLine().trim().equals("1")) {
                System.out.println("Signed out.");
                return;
            }
        }
    }
}
