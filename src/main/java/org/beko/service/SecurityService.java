package org.beko.service;

import org.beko.dto.AuthRequest;
import org.beko.dto.TokenResponse;
import org.beko.model.User;
import org.springframework.stereotype.Service;

@Service
public interface SecurityService {

    /**
     * Registers a new user.
     *
     * @param request the request for register
     */
    User register(AuthRequest request);
    /**
     * Authorizes a user with the provided login and password.
     *
     * @param login    the login of the user
     * @param password the password of the user
     * @return an Optional containing the authorized user if login and password are valid, otherwise empty Optional
     */
    TokenResponse authorize(String login, String password);
}