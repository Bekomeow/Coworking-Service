package org.beko.Service;

import org.beko.Entity.User;

import java.util.HashMap;

public class UserService {
    private HashMap<String, User> users = new HashMap<>();

    public User register(String username, String password) {
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("User already exists.");
        }

        User user = new User(username, password);
        users.put(username, user);
        return user;
    }

    public User login(String username, String password) {
        User user = users.get(username);

        if (user == null || !user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username or password.");
        }
        return user;
    }

    public boolean hasUser(String username) {
        return users.containsKey(username);
    }
}
