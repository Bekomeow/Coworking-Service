package org.beko.DAO;

import org.beko.DAO.impl.UserDAOImpl;
import org.beko.containers.PostgresTestContainer;
import org.beko.liquibase.LiquibaseDemo;
import org.beko.model.User;
import org.beko.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDAOImplTest extends PostgresTestContainer {
    private UserDAOImpl userDAO;

    @BeforeEach
    public void setUp() {
        ConnectionManager connectionManager = new ConnectionManager(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        LiquibaseDemo liquibaseTest = LiquibaseDemo.getInstance();
        liquibaseTest.runMigrations(connectionManager.getConnection());

        userDAO = new UserDAOImpl(connectionManager);
        clearUserTable(connectionManager);
    }

    private void clearUserTable(ConnectionManager connectionManager) {
        String sql = "DELETE FROM coworking.\"User\"";
        try (var connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSave() {
        User user = new User("john_doe", "password123");
        userDAO.save(user);

        List<User> users = userDAO.findAll();
        assertThat(users).hasSize(1);
        User savedUser = users.get(0);
        assertThat(savedUser.getUsername()).isEqualTo("john_doe");
        assertThat(savedUser.getPassword()).isEqualTo("password123");
    }

    @Test
    public void testFindById() {
        User user = new User("john_doe", "password123");
        userDAO.save(user);

        User savedUser = userDAO.findAll().get(0);
        User foundUser = userDAO.findById(savedUser.getId());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(savedUser.getId());
        assertThat(foundUser.getUsername()).isEqualTo("john_doe");
        assertThat(foundUser.getPassword()).isEqualTo("password123");
    }

    @Test
    public void testFindAll() {
        User user1 = new User("john_doe", "password123");
        User user2 = new User("jane_doe", "password456");
        userDAO.save(user1);
        userDAO.save(user2);

        List<User> users = userDAO.findAll();
        assertThat(users).hasSize(2);
        assertThat(users).extracting("username").containsExactlyInAnyOrder("john_doe", "jane_doe");
    }

    @Test
    public void testUpdate() {
        User user = new User("john_doe", "password123");
        userDAO.save(user);

        User savedUser = userDAO.findAll().get(0);
        savedUser.setUsername("updated_john");
        savedUser.setPassword("updated_password");
        userDAO.update(savedUser);

        User updatedUser = userDAO.findById(savedUser.getId());
        assertThat(updatedUser.getUsername()).isEqualTo("updated_john");
        assertThat(updatedUser.getPassword()).isEqualTo("updated_password");
    }

    @Test
    public void testDeleteById() {
        User user = new User("john_doe", "password123");
        userDAO.save(user);

        User savedUser = userDAO.findAll().get(0);
        userDAO.deleteById(savedUser.getId());

        List<User> users = userDAO.findAll();
        assertThat(users).isEmpty();
    }

    @Test
    public void testFindByUsername() {
        User user = new User("john_doe", "password123");
        userDAO.save(user);

        User foundUser = userDAO.findByUsername("john_doe");
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("john_doe");
        assertThat(foundUser.getPassword()).isEqualTo("password123");
    }
}
