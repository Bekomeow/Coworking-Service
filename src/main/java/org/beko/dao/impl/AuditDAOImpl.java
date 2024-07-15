package org.beko.dao.impl;

import lombok.RequiredArgsConstructor;
import org.beko.dao.AuditDAO;
import org.beko.model.Audit;
import org.beko.model.types.ActionType;
import org.beko.model.types.AuditType;
import org.beko.util.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the AuditDAO interface using an in-memory map.
 * Provides methods for CRUD operations on Audit entities.
 */
@Repository
@RequiredArgsConstructor
public class AuditDAOImpl implements AuditDAO {
    private final ConnectionManager connectionManager;

    /**
     * Retrieves all audit records from the database.
     *
     * @return a list of all audit records.
     */
    @Override
    public List<Audit> findAll() {
        List<Audit> audits = new ArrayList<>();
        String sql = "SELECT * FROM coworking.audits";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                audits.add(buildAudit(resultSet));
            }

        } catch (SQLException e) {
            System.err.println("Error during execution of SQL query: " + e.getMessage());
        }

        return audits;
    }

    /**
     * Retrieves an audit record by its ID.
     *
     * @param id the ID of the audit record.
     * @return an Optional containing the audit record if found, or empty if not found.
     */
    @Override
    public Audit findById(Long id) {
        String sqlFindById = """
                SELECT * FROM coworking.audits
                WHERE id=?;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? buildAudit(resultSet)
                    : null;
        } catch (SQLException e) {
            System.err.println("Error during execution of SQL query: " + e.getMessage());
            return null;
        }
    }

    /**
     * Deletes an audit record by its ID.
     *
     * @param id the ID of the audit record.
     */
    @Override
    public void deleteById(Long id) {
        String sqlDeleteById = """
                DELETE FROM coworking.audits
                WHERE id = ?;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteById)) {
            preparedStatement.setObject(1, id);

        } catch (SQLException e) {
            System.err.println("Error during execution of SQL query: " + e.getMessage());
        }
    }

    /**
     * Deletes all audit records from the database.
     *
     * @return true if records were deleted, false otherwise.
     */
    public boolean deleteAll() {
        String sqlDeleteAll = """
            DELETE FROM coworking.audits;
            """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteAll)) {
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error during execution of SQL query: " + e.getMessage());
            return false;
        }
    }

    /**
     * Saves a new audit record to the database.
     *
     * @param audit the audit record to save.
     * @return the saved audit record with its ID set.
     */
    @Override
    public Audit save(Audit audit) {
        String sqlSave = """
            INSERT INTO coworking.audits(username, action_type, audit_type)
            VALUES (?, ?, ?);
            """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, audit.getUsername());
            preparedStatement.setString(2, audit.getActionType().name());
            preparedStatement.setString(3, audit.getAuditType().name());

            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();

            if (keys.next()) {
                audit.setId(keys.getLong(1));
            }

            return audit;
        } catch (SQLException e) {
            System.err.println("Error during execution of SQL query: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates an existing audit record in the database.
     *
     * @param audit the audit record to update.
     */
    @Override
    public void update(Audit audit) {
        String sqlUpdate = """
        UPDATE coworking.audits
        SET username = ?, action_type = ?, audit_type = ?
        WHERE id = ?;
        """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate)) {

            preparedStatement.setString(1, audit.getUsername());
            preparedStatement.setString(2, audit.getActionType().name());
            preparedStatement.setString(3, audit.getAuditType().name());
            preparedStatement.setLong(4, audit.getId());

        } catch (SQLException e) {
            System.err.println("Error during execution of SQL query: " + e.getMessage());
        }
    }

    /**
     * Builds an Audit object from a ResultSet.
     *
     * @param resultSet the ResultSet containing audit data.
     * @return the built Audit object.
     * @throws SQLException if an SQL error occurs.
     */
    private Audit buildAudit(ResultSet resultSet) throws SQLException {
        String actionTypeString = resultSet.getString("action_type");
        ActionType actionType = ActionType.valueOf(actionTypeString);

        String auditTypeString = resultSet.getString("audit_type");
        AuditType auditType = AuditType.valueOf(auditTypeString);

        return Audit.builder()
                .id(resultSet.getLong("id"))
                .auditType(auditType)
                .auditTimestamp(resultSet.getTimestamp("audit_timestamp").toLocalDateTime())
                .actionType(actionType)
                .username(resultSet.getString("username"))
                .build();
    }
}