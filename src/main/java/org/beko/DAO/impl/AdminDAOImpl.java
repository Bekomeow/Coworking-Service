package org.beko.DAO.impl;

import lombok.RequiredArgsConstructor;
import org.beko.DAO.AdminDAO;
import org.beko.model.Admin;
import org.beko.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the AdminDAO interface for managing Admin entities in the database.
 */
@RequiredArgsConstructor
public class AdminDAOImpl implements AdminDAO {
    private final ConnectionManager connectionManager;
    /**
     * Saves a new Admin entity to the database.
     *
     * @param admin the Admin entity to be saved
     */
    @Override
    public void save(Admin admin) {
        String sql = "INSERT INTO coworking.\"Admin\" (admin_name, password) VALUES (?, ?)";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, admin.getAdminName());
            statement.setString(2, admin.getAdminPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds an Admin entity by its ID.
     *
     * @param id the ID of the Admin entity to find
     * @return the found Admin entity, or null if not found
     */
    @Override
    public Admin findById(Long id) {
        String sql = "SELECT * FROM coworking.\"Admin\" WHERE id = ?";
        Admin admin = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                admin = new Admin(resultSet.getLong("id"),
                        resultSet.getString("admin_name"),
                        resultSet.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }

    /**
     * Finds all Admin entities in the database.
     *
     * @return a list of all Admin entities
     */
    @Override
    public List<Admin> findAll() {
        String sql = "SELECT * FROM coworking.\"Admin\"";
        List<Admin> admins = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Admin admin = new Admin(resultSet.getLong("id"),
                        resultSet.getString("admin_name"),
                        resultSet.getString("password"));
                admins.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admins;
    }

    /**
     * Updates an existing Admin entity in the database.
     *
     * @param admin the Admin entity with updated information
     */
    @Override
    public void update(Admin admin) {
        String sql = "UPDATE coworking.\"Admin\" SET admin_name = ?, password = ? WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, admin.getAdminName());
            statement.setString(2, admin.getAdminPassword());
            statement.setLong(3, admin.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes an Admin entity by its ID.
     *
     * @param id the ID of the Admin entity to be deleted
     */
    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM coworking.\"Admin\" WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
