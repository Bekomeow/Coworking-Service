package org.beko;

import org.beko.Service.AdminService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdminServiceTest {
    private AdminService adminService = new AdminService();

    @Test
    public void testLoginAdminSuccessfully() {
        assertDoesNotThrow(() -> adminService.login("Admin", "Admin"));
    }

    @Test
    public void testLoginAdminInvalidCredentials() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.login("Admin", "WrongPassword");
        });
        assertEquals("Invalid admin name or password.", exception.getMessage());
    }
}
