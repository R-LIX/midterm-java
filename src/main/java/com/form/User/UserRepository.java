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
        csv.addRecord(user.toString());
        users.put(user.getUsername(), user.getPassword());
    }

    public void deleteUser(String username) {
        csv.deleteRecordByKey(username);
        users.remove(username);
    }

}
