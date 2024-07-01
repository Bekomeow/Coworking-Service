package org.beko.service;

import org.beko.DAO.impl.UserDAOImpl;
import org.beko.model.User;
import org.beko.util.ConnectionManager;

import java.util.Optional;

/**
 * Service class for handling user operations.
 */
public class UserService {
    private final UserDAOImpl USER_DAO;

    public UserService(ConnectionManager connectionManager) {
        USER_DAO = new UserDAOImpl(connectionManager);
    }

    /**
     * Registers a new user with the specified username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return the created User object
     * @throws IllegalArgumentException if the user already exists
     */
    public User register(String username, String password) {
        if (USER_DAO.findByUsername(username) != null) {
            throw new IllegalArgumentException("User already exists.");
        }

        User user = new User(username, password);
        USER_DAO.save(user);
        return user;
    }

    /**
     * Logs in a user with the specified username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return the logged-in User object
     * @throws IllegalArgumentException if the username or password is invalid
     */
    public User login(String username, String password) {
        User user = USER_DAO.findByUsername(username);

        if (user == null || !user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username or password.");
        }
        return user;
    }

    /**
     * Checks if a user exists by their username.
     *
     * @param username the username of the user
     * @return true if the user exists, false otherwise
     */
    public boolean hasUser(String username) {
        Optional<User> maybeUser = Optional.ofNullable(USER_DAO.findByUsername(username));
        return maybeUser.isPresent();
    }
}
