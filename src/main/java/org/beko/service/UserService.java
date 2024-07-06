package org.beko.service;

import org.beko.model.User;

public interface UserService {
    /**
     * Registers a new user with the specified username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return the created User object
     * @throws IllegalArgumentException if the user already exists
     */
    User register(String username, String password);

    /**
     * Logs in a user with the specified username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return the logged-in User object
     * @throws IllegalArgumentException if the username or password is invalid
     */
    User login(String username, String password);

    /**
     * Checks if a user exists by their username.
     *
     * @param username the username of the user
     * @return true if the user exists, false otherwise
     */
    boolean hasUser(String username);
}
