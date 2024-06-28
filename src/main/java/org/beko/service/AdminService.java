package org.beko.service;

import org.beko.DAO.impl.AdminDAOImpl;
import org.beko.model.Admin;

import java.util.List;
import java.util.Optional;

public class AdminService {
    private static final AdminDAOImpl ADMIN_DAO = new AdminDAOImpl();
    public boolean login(String adminName, String password) {
        List<Admin> admins = ADMIN_DAO.findAll();
        Optional<Admin> admin = admins.stream()
                .filter(a -> a.getAdminName().equals(adminName) && a.getAdminPassword().equals(password))
                .findFirst();

        return admin.isPresent();
    }
}
