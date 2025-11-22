package com.example.project02;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final List<User> users = new ArrayList<>();

    public UserRepository() {
        // Demo data — replace with Room later
        users.add(new User("demo", "password", false));
        users.add(new User("admin", "admin123", true));
    }

    public User authenticate(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
}
