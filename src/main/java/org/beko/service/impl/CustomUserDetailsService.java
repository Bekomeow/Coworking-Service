package org.beko.service.impl;

import lombok.RequiredArgsConstructor;
import org.beko.dao.UserDAO;
import org.beko.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(userDAO.findByUsername(username));
        return (UserDetails) user.orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователь по логину '%s' не найден", username)
        ));
    }
}