package org.beko.service;

import org.beko.containers.PostgresTestContainer;
import org.beko.liquibase.LiquibaseDemo;
import org.beko.service.impl.AdminServiceImpl;
import org.beko.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

class AdminServiceImplTest extends PostgresTestContainer {
    private AdminServiceImpl adminService;

    @BeforeEach
    public void setUp() {
        ConnectionManager connectionManager = new ConnectionManager(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        LiquibaseDemo liquibaseTest = LiquibaseDemo.getInstance();
        liquibaseTest.runMigrations(connectionManager.getConnection());

        adminService = new AdminServiceImpl(connectionManager);
    }

    @Test
    void testLoginSuccessful() {
        boolean result = adminService.login("admin", "admin");

        assertThat(result).isTrue();
    }

    @Test
    void testLoginFailed() {
        boolean result = adminService.login("admin", "wrongPassword");

        assertThat(result).isFalse();
    }
}
