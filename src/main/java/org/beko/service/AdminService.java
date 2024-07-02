package org.beko.service;

public interface AdminService {
    /**
     * Logs in an admin with the specified name and password.
     *
     * @param adminName the admin name
     * @param password  the admin password
     * @return true if the admin is successfully logged in, false otherwise
     */
    boolean login(String adminName, String password);
}
