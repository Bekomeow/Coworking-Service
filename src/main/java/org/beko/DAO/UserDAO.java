package org.beko.DAO;

import org.beko.model.Booking;
import org.beko.model.User;
import java.util.List;

public interface UserDAO extends DAO<Long, User> {
    public User findByUsername(String username);
}
