package org.beko.service;

import org.beko.model.User;

public interface UserService {
    boolean hasUser(String username);

    User getUserByName(String name);
}
