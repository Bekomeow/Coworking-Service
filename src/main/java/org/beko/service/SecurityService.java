package org.beko.service;

import org.beko.dto.TokenResponse;
import org.beko.model.User;

public interface SecurityService {

    /**
     * Registers a new user.
     *
     * @param login    the login of the user
     * @param password the password of the user
     */
    User register(String login, String password);
    /**
     * Authorizes a user with the provided login and password.
     *
     * @param login    the login of the user
     * @param password the password of the user
     * @return an Optional containing the authorized user if login and password are valid, otherwise empty Optional
     */
    TokenResponse authorize(String login, String password);
}