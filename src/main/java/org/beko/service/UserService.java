package org.beko.service;

import org.beko.DAO.impl.UserDAOImpl;
import org.beko.model.User;

import java.util.Optional;

public class UserService {
    private static final UserDAOImpl USER_DAO = new UserDAOImpl();

    public User register(String username, String password) {
        if (USER_DAO.findByUsername(username) != null) {
            throw new IllegalArgumentException("User already exists.");
        }

        User user = new User(username, password);
        USER_DAO.save(user);
        return user;
    }

    public User login(String username, String password) {
        User user = USER_DAO.findByUsername(username);

        if (user == null || !user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username or password.");
        }
        return user;
    }

    public boolean hasUser(String username) {
        Optional<User> maybeUser = Optional.ofNullable(USER_DAO.findByUsername(username));
        return maybeUser.isPresent();
    }
}
