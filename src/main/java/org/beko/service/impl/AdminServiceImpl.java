package org.beko.service.impl;

import org.beko.DAO.impl.AdminDAOImpl;
import org.beko.model.Admin;
import org.beko.service.AdminService;
import org.beko.util.ConnectionManager;

import java.util.List;
import java.util.Optional;

/**
 * Service class for handling admin operations.
 */
public class AdminServiceImpl implements AdminService {
    private final AdminDAOImpl ADMIN_DAO;

    public AdminServiceImpl(ConnectionManager connectionManager) {
        ADMIN_DAO = new AdminDAOImpl(connectionManager);
    }

    /**
     * Logs in an admin with the specified name and password.
     *
     * @param adminName the admin name
     * @param password  the admin password
     * @return true if the admin is successfully logged in, false otherwise
     */
    public boolean login(String adminName, String password) {
        List<Admin> admins = ADMIN_DAO.findAll();
        Optional<Admin> admin = admins.stream()
                .filter(a -> a.getAdminName().equals(adminName) && a.getAdminPassword().equals(password))
                .findFirst();

        return admin.isPresent();
    }
}
