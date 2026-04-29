package com.form;

import com.form.User.DuplicateUserException;
import com.form.User.User;
import com.form.User.UserNotFoundException;
import com.form.User.UserRepository;
import com.form.password.Password;

public class FormService {
    private final UserRepository userRepository = new UserRepository();

    public boolean signIn(String username, String password) {
        password = Password.hash(password);
        var user = new User(username, password);
        try {
            if (userRepository.exists(user)) {
                var realUser = userRepository.getUser(user.getUsername());
                return realUser.equals(user);
            }
        } catch (UserNotFoundException e) {
            return false;
        }
        return false;
    }

    public boolean usernameExists(String username) {
        return userRepository.usernameExists(username);
    }

    public boolean signUp(String username, String password) {
        try {
            var user = new User(username, Password.hash(password));
            userRepository.saveUser(user);
            return true;
        } catch (DuplicateUserException e) {
            return false;
        }
    }






}
