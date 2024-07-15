package org.beko.service.impl;

import lombok.AllArgsConstructor;
import org.beko.annotations.Auditable;
import org.beko.dao.UserDAO;
import org.beko.dao.impl.UserDAOImpl;
import org.beko.model.User;
import org.beko.model.types.ActionType;
import org.beko.service.UserService;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.util.Optional;

/**
 * Service class for handling user operations.
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO USER_DAO;

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

    @Auditable(actionType = ActionType.LOGOUT)
    public void logout(ServletContext servletContext) {
        servletContext.removeAttribute("authentication");
    }
}
