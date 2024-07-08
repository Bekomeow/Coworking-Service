package org.beko.service.impl;

import lombok.AllArgsConstructor;
import org.beko.dao.impl.UserDAOImpl;
import org.beko.model.User;
import org.beko.service.UserService;

import java.util.Optional;

/**
 * Service class for handling user operations.
 */
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAOImpl USER_DAO;

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

    @Override
    public User getUserByName(String username) {
        return USER_DAO.findByUsername(username);
    }
}
