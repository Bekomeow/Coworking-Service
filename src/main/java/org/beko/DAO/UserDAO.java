package org.beko.DAO;

import org.beko.model.User;

/**
 * DAO interface for User entity operations.
 */
public interface UserDAO extends DAO<Long, User> {
    /**
     * Finds a user by username.
     *
     * @param username the username of the user
     * @return the User object for the specified username
     */
    public User findByUsername(String username);
}
