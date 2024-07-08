package org.beko.service;

import org.beko.containers.PostgresTestContainer;
import org.beko.dao.impl.UserDAOImpl;
import org.beko.liquibase.LiquibaseDemo;
import org.beko.model.User;
import org.beko.service.impl.UserServiceImpl;
import org.beko.util.ConnectionManager;
import org.beko.util.PropertiesUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceImplTest extends PostgresTestContainer {
    private UserServiceImpl userService;
    private UserDAOImpl userDAO;
    private ConnectionManager connectionManager;

    @BeforeEach
    public void setUp() {
        connectionManager = new ConnectionManager(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );

        String changeLogFile = PropertiesUtil.get("liquibase.change-log");
        String schemaName = PropertiesUtil.get("liquibase.liquibase-schema");

        LiquibaseDemo liquibaseDemo = new LiquibaseDemo(connectionManager.getConnection(), changeLogFile, schemaName);
        liquibaseDemo.runMigrations();

        userDAO = new UserDAOImpl(connectionManager);
        userService = new UserServiceImpl(userDAO);

        clearUserTable();
        resetSequence();
    }

    private void clearUserTable() {
        String sql = "DELETE FROM coworking.\"user\"";
        try (var connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void resetSequence() {
        String sql = "ALTER SEQUENCE coworking.\"user_id_seq\" RESTART WITH 1";
        try (var connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testHasUser() {
        User user = User.builder()
                .id(1L)
                .username("user1")
                .password("password")
                .build();

        userDAO.save(user);

        boolean userExists = userService.hasUser("user1");
        boolean userNotExists = userService.hasUser("user2");

        assertThat(userExists).isTrue();
        assertThat(userNotExists).isFalse();
    }

    @Test
    void testGetUserByName() {
        User user = User.builder()
                .id(1L)
                .username("user1")
                .password("password")
                .build();

        userDAO.save(user);

        User foundUser = userService.getUserByName("user1");
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("user1");

        User notFoundUser = userService.getUserByName("user2");
        assertThat(notFoundUser).isNull();
    }
}
