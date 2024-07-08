//package org.beko.service;
//
//import org.beko.dao.impl.UserDAOImpl;
//import org.beko.containers.PostgresTestContainer;
//import org.beko.liquibase.LiquibaseDemo;
//import org.beko.model.User;
//import org.beko.service.impl.UserServiceImpl;
//import org.beko.util.ConnectionManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import java.sql.SQLException;
//import java.sql.Statement;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//class UserServiceImplTest extends PostgresTestContainer {
//    private UserServiceImpl userService;
//    private UserDAOImpl userDAO;
//    private ConnectionManager connectionManager;
//
//    @BeforeEach
//    public void setUp() {
//        connectionManager = new ConnectionManager(
//                container.getJdbcUrl(),
//                container.getUsername(),
//                container.getPassword()
//        );
//        LiquibaseDemo liquibaseTest = LiquibaseDemo.getInstance();
//        liquibaseTest.runMigrations(connectionManager.getConnection());
//
//        userDAO = new UserDAOImpl(connectionManager);
//        userService = new UserServiceImpl(connectionManager);
//        clearUserTable();
//        resetSequence();
//    }
//
//    private void clearUserTable() {
//        String sql = "DELETE FROM coworking.\"user\"";
//        try (var connection = connectionManager.getConnection();
//             Statement statement = connection.createStatement()) {
//            statement.executeUpdate(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void resetSequence() {
//        String sql = "ALTER SEQUENCE coworking.\"user_id_seq\" RESTART WITH 1";
//        try (var connection = connectionManager.getConnection();
//             Statement statement = connection.createStatement()) {
//            statement.executeUpdate(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    void testRegisterUser() {
//        User user = userService.register("testUser", "password");
//
//        assertThat(user).isNotNull();
//        assertThat(user.getUsername()).isEqualTo("testUser");
//        assertThat(user.getPassword()).isEqualTo("password");
//
//        User foundUser = userDAO.findByUsername("testUser");
//        assertThat(foundUser).isNotNull();
//        assertThat(foundUser.getUsername()).isEqualTo("testUser");
//        assertThat(foundUser.getPassword()).isEqualTo("password");
//    }
//
//    @Test
//    void testRegisterExistingUser() {
//        userService.register("testUser", "password");
//
//        assertThatThrownBy(() -> userService.register("testUser", "password"))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("User already exists.");
//    }
//
//    @Test
//    void testLoginUser() {
//        userService.register("testUser", "password");
//
//        User user = userService.login("testUser", "password");
//
//        assertThat(user).isNotNull();
//        assertThat(user.getUsername()).isEqualTo("testUser");
//        assertThat(user.getPassword()).isEqualTo("password");
//    }
//
//    @Test
//    void testLoginInvalidUser() {
//        userService.register("testUser", "password");
//
//        assertThatThrownBy(() -> userService.login("testUser", "wrongPassword"))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("Invalid username or password.");
//
//        assertThatThrownBy(() -> userService.login("nonExistentUser", "password"))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("Invalid username or password.");
//    }
//
//    @Test
//    void testHasUser() {
//        userService.register("testUser", "password");
//
//        boolean exists = userService.hasUser("testUser");
//        boolean notExists = userService.hasUser("nonExistentUser");
//
//        assertThat(exists).isTrue();
//        assertThat(notExists).isFalse();
//    }
//}
