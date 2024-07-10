//package org.beko.dao;
//
//import org.beko.dao.impl.AuditDAOImpl;
//import org.beko.containers.PostgresTestContainer;
//import org.beko.liquibase.LiquibaseDemo;
//import org.beko.model.Audit;
//import org.beko.model.types.ActionType;
//import org.beko.model.types.AuditType;
//import org.beko.util.ConnectionManager;
//import org.beko.util.PropertiesUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class AuditDAOImplTest extends PostgresTestContainer {
//    private AuditDAOImpl auditDAO;
//
//    @BeforeEach
//    public void setUp() {
//        ConnectionManager connectionManager = new ConnectionManager(
//                container.getJdbcUrl(),
//                container.getUsername(),
//                container.getPassword()
//        );
//
//        String changeLogFile = PropertiesUtil.get("liquibase.change-log");
//        String schemaName = PropertiesUtil.get("liquibase.liquibase-schema");
//
//        LiquibaseDemo liquibaseDemo = new LiquibaseDemo(connectionManager.getConnection(), changeLogFile, schemaName);
//        liquibaseDemo.runMigrations();
//
//        auditDAO = new AuditDAOImpl(connectionManager);
//        clearAuditTable(connectionManager);
//    }
//
//    private void clearAuditTable(ConnectionManager connectionManager) {
//        String sql = "DELETE FROM coworking.\"audit\"";
//        try (var connection = connectionManager.getConnection();
//             Statement statement = connection.createStatement()) {
//            statement.executeUpdate(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    @DisplayName("Save Audit and Verify")
//    public void testSave() {
//        Audit audit = new Audit(null, "user1", AuditType.SUCCESS, ActionType.AUTHORIZATION);
//        auditDAO.save(audit);
//
//        List<Audit> audits = auditDAO.findAll();
//
//        assertThat(audits).hasSize(1);
//        Audit savedAudit = audits.get(0);
//        assertThat(savedAudit.getLogin()).isEqualTo("user1");
//        assertThat(savedAudit.getAuditType()).isEqualTo(AuditType.SUCCESS);
//        assertThat(savedAudit.getActionType()).isEqualTo(ActionType.AUTHORIZATION);
//    }
//
//    @Test
//    @DisplayName("Find Audit by ID")
//    public void testFindById() {
//        Audit audit = new Audit(null, "user1", AuditType.SUCCESS, ActionType.AUTHORIZATION);
//        auditDAO.save(audit);
//
//        Audit savedAudit = auditDAO.findAll().get(0);
//        Audit foundAudit = auditDAO.findById(savedAudit.getId());
//
//        assertThat(foundAudit).isNotNull();
//        assertThat(foundAudit.getId()).isEqualTo(savedAudit.getId());
//        assertThat(foundAudit.getLogin()).isEqualTo("user1");
//        assertThat(foundAudit.getAuditType()).isEqualTo(AuditType.SUCCESS);
//        assertThat(foundAudit.getActionType()).isEqualTo(ActionType.AUTHORIZATION);
//    }
//
//    @Test
//    @DisplayName("Find All Audits")
//    public void testFindAll() {
//        Audit audit1 = new Audit(null, "user1", AuditType.SUCCESS, ActionType.AUTHORIZATION);
//        Audit audit2 = new Audit(null, "user2", AuditType.FAIL, ActionType.REGISTRATION);
//
//        auditDAO.save(audit1);
//        auditDAO.save(audit2);
//
//        List<Audit> audits = auditDAO.findAll();
//        assertThat(audits).hasSize(2);
//        assertThat(audits).extracting("login").containsExactlyInAnyOrder("user1", "user2");
//    }
//
//    @Test
//    @DisplayName("Update Audit and Verify")
//    public void testUpdate() {
//        Audit audit = new Audit(null, "user1", AuditType.SUCCESS, ActionType.AUTHORIZATION);
//        auditDAO.save(audit);
//
//        Audit savedAudit = auditDAO.findAll().get(0);
//        savedAudit.setLogin("updatedUser");
//        auditDAO.update(savedAudit);
//
//        Audit updatedAudit = auditDAO.findById(savedAudit.getId());
//        assertThat(updatedAudit.getLogin()).isEqualTo("updatedUser");
//    }
//
//    @Test
//    @DisplayName("Delete Audit by ID")
//    public void testDeleteById() {
//        Audit audit = new Audit(null, "user1", AuditType.SUCCESS, ActionType.AUTHORIZATION);
//        auditDAO.save(audit);
//
//        Audit savedAudit = auditDAO.findAll().get(0);
//        auditDAO.deleteById(savedAudit.getId());
//
//        List<Audit> audits = auditDAO.findAll();
//        assertThat(audits).isEmpty();
//    }
//}
