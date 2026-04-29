package com.form;

import com.form.User.User;

import java.util.Scanner;

public class FormApplication {
    private final FormService formService = new FormService();
    private final Scanner  scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new FormApplication().run();
    }

    public void run() {

        formService.signUp(new User("123", "123"));

    }

}
