package org.beko.service.impl;

import lombok.RequiredArgsConstructor;
import org.beko.annotations.Auditable;
import org.beko.dao.UserDAO;
import org.beko.dto.AuthRequest;
import org.beko.dto.TokenResponse;
import org.beko.exception.InvalidCredentialsException;
import org.beko.exception.NotValidArgumentException;
import org.beko.exception.RegisterException;
import org.beko.model.User;
import org.beko.model.types.ActionType;
import org.beko.security.JwtTokenUtils;
import org.beko.service.SecurityService;
import org.beko.util.PasswordUtil;
import org.beko.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for security of app.
 */
@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {
    private final UserDAO userDao;
    private final JwtTokenUtils jwtTokenUtil;

    /**
     * Registers a new user with the provided login and password.
     *
     * @param request the auth request
     * @return the registered user DTO
     * @throws NotValidArgumentException if login or password is empty, blank, or does not meet length requirements
     * @throws RegisterException if a user with the same login already exists
     */
    @Auditable(actionType = ActionType.REGISTRATION)
    public User register(AuthRequest request) {
        ValidationUtil.validate(request);

        String username = request.username();
        String password = request.password();
        if (username == null || password == null || username.isEmpty() || password.isEmpty() || username.isBlank() || password.isBlank()) {
            throw new NotValidArgumentException("Пароль или логин не могут быть пустыми или состоять только из пробелов.");
        }

        if (password.length() < 5 || password.length() > 30) {
            throw new NotValidArgumentException("Длина пароля должна составлять от 5 до 30 символов.");
        }

        Optional<User> optionalUser = Optional.ofNullable(userDao.findByUsername(username));
        if (optionalUser.isPresent()) {
            throw new RegisterException("A user with this username already exists.");
        }

        User newUser = User.builder()
                .username(username)
                .password(password)
                .build();

        return userDao.save(newUser);
    }

    /**
     * Authenticate a user with the provided username and password.
     *
     * @param request the auth request
     * @return a token response containing the generated JWT token
     * @throws InvalidCredentialsException if the user is not found or the password is incorrect
     */
    @Auditable(actionType = ActionType.AUTHORIZATION)
    public TokenResponse authenticate(AuthRequest request) {
        String username = request.username();
        String password = request.password();

        Optional<User> optionalUser = Optional.ofNullable(userDao.findByUsername(username));

        if (optionalUser.isEmpty() || !PasswordUtil.checkPassword(password, optionalUser.get().getPassword())) {
            throw new InvalidCredentialsException("Incorrect username or password.");
        }

        String token = jwtTokenUtil.generateToken(username);
        return new TokenResponse(token);
    }
}