package org.beko.dao.impl;

import lombok.RequiredArgsConstructor;
import org.beko.dao.UserDAO;
import org.beko.model.User;
import org.beko.model.types.Role;
import org.beko.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the UserDAO interface for managing User entities in the database.
 */
@RequiredArgsConstructor
public class UserDAOImpl implements UserDAO {
    private final ConnectionManager connectionManager;
    /**
     * Saves a new User entity to the database.
     *
     * @param user the User entity to be saved
     */
    @Override
    public User save(User user) {
        String sql = "INSERT INTO coworking.\"user\" (username, password, role) VALUES (?, ?, ?)";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().toString());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getObject(1, Long.class));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Finds a User entity by its ID.
     *
     * @param id the ID of the User entity to find
     * @return the found User entity, or null if not found
     */
    @Override
    public User findById(Long id) {
        String sql = "SELECT * FROM coworking.\"user\" WHERE id = ?";
        User user = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new User(resultSet.getLong("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        Role.valueOf(resultSet.getString("role")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Finds all User entities in the database.
     *
     * @return a list of all User entities
     */
    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM coworking.\"user\"";
        List<User> users = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User(resultSet.getLong("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        Role.valueOf(resultSet.getString("role")));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Updates an existing User entity in the database.
     *
     * @param user the User entity with updated information
     */
    @Override
    public void update(User user) {
        String sql = "UPDATE coworking.\"user\" SET username = ?, password = ?, role = ? WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().toString());
            statement.setLong(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a User entity by its ID.
     *
     * @param id the ID of the User entity to be deleted
     */
    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM coworking.\"user\" WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds a User entity by its username.
     *
     * @param username the username of the User entity to find
     * @return the found User entity, or null if not found
     */
    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM coworking.\"user\" WHERE username = ?";
        User user = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new User(resultSet.getLong("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        Role.valueOf(resultSet.getString("role")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
