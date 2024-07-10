package org.beko.service;

import org.beko.containers.PostgresTestContainer;
import org.beko.dao.impl.AuditDAOImpl;
import org.beko.liquibase.LiquibaseDemo;
import org.beko.model.Audit;
import org.beko.model.types.ActionType;
import org.beko.model.types.AuditType;
import org.beko.service.impl.AuditServiceImpl;
import org.beko.util.ConnectionManager;
import org.beko.util.PropertiesUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AuditServiceImplTest extends PostgresTestContainer {
    private AuditServiceImpl auditService;
    private AuditDAOImpl auditDAO;
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

        auditDAO = new AuditDAOImpl(connectionManager);
        auditService = new AuditServiceImpl(auditDAO);
        clearAuditTable();
        resetSequence();
    }

    private void clearAuditTable() {
        String sql = "DELETE FROM coworking.\"audit\"";
        try (var connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetSequence() {
        String sql = "ALTER SEQUENCE coworking.\"audit_id_seq\" RESTART WITH 1";
        try (var connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSave() {
        Audit audit = new Audit(1L, "user1", AuditType.SUCCESS, ActionType.AUTHORIZATION);
        Audit savedAudit = auditService.save(audit);

        assertThat(savedAudit).isNotNull();
        assertThat(savedAudit.getId()).isNotNull();
        assertThat(savedAudit.getLogin()).isEqualTo("user1");
        assertThat(savedAudit.getAuditType()).isEqualTo(AuditType.SUCCESS);
        assertThat(savedAudit.getActionType()).isEqualTo(ActionType.AUTHORIZATION);
    }

    @Test
    void testShowAllAudits() {
        Audit audit1 = new Audit(null, "user1", AuditType.SUCCESS, ActionType.AUTHORIZATION);
        Audit audit2 = new Audit(null, "user2", AuditType.FAIL, ActionType.REGISTRATION);

        auditService.save(audit1);
        auditService.save(audit2);

        List<Audit> audits = auditService.showAllAudits();
        assertThat(audits).hasSize(2);
        assertThat(audits).extracting("login").containsExactlyInAnyOrder("user1", "user2");
    }

    @Test
    void testAudit() {
        Audit audit = auditService.audit("user1", ActionType.AUTHORIZATION, AuditType.SUCCESS);

        assertThat(audit).isNotNull();
        assertThat(audit.getLogin()).isEqualTo("user1");
        assertThat(audit.getAuditType()).isEqualTo(AuditType.SUCCESS);
        assertThat(audit.getActionType()).isEqualTo(ActionType.AUTHORIZATION);
    }
}
