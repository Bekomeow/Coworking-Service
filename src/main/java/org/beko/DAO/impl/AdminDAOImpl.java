package org.beko.DAO.impl;

import org.beko.DAO.AdminDAO;
import org.beko.model.Admin;
import org.beko.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAOImpl implements AdminDAO {

    @Override
    public void save(Admin admin) {
        String sql = "INSERT INTO coworking.Admin (admin_name, password) VALUES (?, ?)";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, admin.getAdminName());
            statement.setString(2, admin.getAdminPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Admin findById(Long id) {
        String sql = "SELECT * FROM coworking.Admin WHERE id = ?";
        Admin admin = null;
        try (Connection connection = ConnectionManager.getConnection();
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

    @Override
    public List<Admin> findAll() {
        String sql = "SELECT * FROM coworking.Admin";
        List<Admin> admins = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
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

    @Override
    public void update(Admin admin) {
        String sql = "UPDATE coworking.Admin SET admin_name = ?, password = ? WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, admin.getAdminName());
            statement.setString(2, admin.getAdminPassword());
            statement.setLong(3, admin.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM coworking.Admin WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
