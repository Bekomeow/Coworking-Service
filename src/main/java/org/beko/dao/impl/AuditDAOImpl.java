package org.beko.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.beko.dao.AuditDAO;
import org.beko.model.Audit;
import org.beko.model.types.ActionType;
import org.beko.model.types.AuditType;
import org.beko.util.ConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the AuditDAO interface using an in-memory map.
 * Provides methods for CRUD operations on Audit entities.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AuditDAOImpl implements AuditDAO {
    private final ConnectionManager connectionManager;

    @Override
    public Audit findById(Long id) {
        String sql = "SELECT * FROM coworking.\"audit\" WHERE id = ?";
        Audit audit = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                audit = new Audit(
                        resultSet.getLong("id"),
                        resultSet.getString("login"),
                        AuditType.valueOf(resultSet.getString("audit_type")),
                        ActionType.valueOf(resultSet.getString("action_type"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return audit;
    }

    /**
     * Retrieves a list of all Audit entities.
     *
     * @return A list containing all Audit entities stored in the in-memory map.
     */
    @Override
    public List<Audit> findAll() {
        String sql = "SELECT * FROM coworking.\"audit\"";
        List<Audit> audits = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Audit audit = new Audit(
                        resultSet.getLong("id"),
                        resultSet.getString("login"),
                        AuditType.valueOf(resultSet.getString("audit_type")),
                        ActionType.valueOf(resultSet.getString("action_type"))
                );
                audits.add(audit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return audits;
    }

    /**
     * Saves an Audit entity to the in-memory storage.
     *
     * @param audit The Audit entity to save.
     * @return The saved Audit entity with an assigned ID.
     */
    @Override
    public Audit save(Audit audit) {
        String sql = "INSERT INTO coworking.\"audit\" (login, audit_type, action_type) VALUES (?,?,?);";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, audit.getLogin());
            statement.setString(2, audit.getAuditType().toString());
            statement.setString(3, audit.getActionType().toString());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    audit.setId(keys.getObject(1, Long.class));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return audit;
    }

    @Override
    public void update(Audit audit) {
        String sql = "UPDATE coworking.\"audit\" SET login = ?, audit_type = ?, action_type = ? WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, audit.getLogin());
            statement.setObject(2, audit.getAuditType(), Types.OTHER);
            statement.setObject(3, audit.getActionType(), Types.OTHER);
            statement.setLong(4, audit.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM coworking.\"audit\" WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        String sql = "DELETE FROM coworking.\"audit\"";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}