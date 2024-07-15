package org.beko.service;

import org.beko.model.User;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;

@Service
public interface UserService {
    boolean hasUser(String username);

    User getUserByName(String name);

    void logout(ServletContext servletContext);
}
