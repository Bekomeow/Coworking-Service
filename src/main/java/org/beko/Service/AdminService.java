package org.beko.Service;

import org.beko.Entity.Admin;

public class AdminService {
    public void login(String adminName, String password) {
        if (!(Admin.ADMIN_NAME.equals(adminName) && Admin.ADMIN_PASSWORD.equals(password))) {
            throw new IllegalArgumentException("Invalid admin name or password.");
        }
    }
}
