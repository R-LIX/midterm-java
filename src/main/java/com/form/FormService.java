package com.form;

import com.form.User.User;
import com.form.User.UserRepository;

public class FormService {
    private final UserRepository userRepository = new UserRepository();

    public boolean signIn(User user) {
        return userRepository.exists(user);
    }

    public void signUp(User user) {
        userRepository.saveUser(user);
    }


}
