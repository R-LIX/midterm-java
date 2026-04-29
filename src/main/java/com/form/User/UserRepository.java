package com.form.User;

import com.form.database.Csv;

import java.io.File;
import java.util.*;

public class UserRepository {
    private final Csv csv = new Csv(new File("user.csv"), Arrays.asList("username", "password"));
    private final Map<String, String> users = csv.loadCsvToMap();

    public  Map<String, String> getAllUsers() {
        return users;
    }

    public void saveUser(User user) {
        if (users.containsKey(user.getUsername())) {
            throw new DuplicateUserException(user.getUsername());
        }
        csv.addRecord(user.toString());
        users.put(user.getUsername(), user.getPassword());
    }

    public void deleteUser(String username) {
        csv.deleteRecordByKey(username);
        users.remove(username);
    }

    public User getUser(String username) {
        return new User(username, users.get(username));
    }

    public boolean usernameExists(String username) {
        return users.containsKey(username);
    }

    public boolean exists(User user) {
        if (users.containsKey(user.getUsername())) {
            return true;
        }
        throw new UserNotFoundException(user.getUsername());
    }


}
