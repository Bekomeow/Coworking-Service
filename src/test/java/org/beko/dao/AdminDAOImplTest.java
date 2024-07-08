//package org.beko.dao;
//
//import org.beko.dao.impl.AdminDAOImpl;
//import org.beko.containers.PostgresTestContainer;
//import org.beko.liquibase.LiquibaseDemo;
//import org.beko.model.Admin;
//import org.beko.util.ConnectionManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class AdminDAOImplTest extends PostgresTestContainer {
//    private AdminDAOImpl adminDAO;
//
//    @BeforeEach
//    public void setUp() {
//        ConnectionManager connectionManager = new ConnectionManager(
//                container.getJdbcUrl(),
//                container.getUsername(),
//                container.getPassword()
//        );
//        LiquibaseDemo liquibaseTest = LiquibaseDemo.getInstance();
//        liquibaseTest.runMigrations(connectionManager.getConnection());
//
//        adminDAO = new AdminDAOImpl(connectionManager);
//        clearAdminTable(connectionManager);
//    }
//
//    private void clearAdminTable(ConnectionManager connectionManager) {
//        String sql = "DELETE FROM coworking.\"admin\"";
//        try (var connection = connectionManager.getConnection();
//             Statement statement = connection.createStatement()) {
//            statement.executeUpdate(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testSave() {
//        Admin admin = new Admin(null, "admin1", "password1");
//        adminDAO.save(admin);
//
//        List<Admin> admins = adminDAO.findAll();
//        assertThat(admins).hasSize(1);
//        Admin savedAdmin = admins.get(0);
//        assertThat(savedAdmin.getAdminName()).isEqualTo("admin1");
//        assertThat(savedAdmin.getAdminPassword()).isEqualTo("password1");
//    }
//
//    @Test
//    public void testFindById() {
//        Admin admin = new Admin(null, "admin1", "password1");
//        adminDAO.save(admin);
//
//        Admin savedAdmin = adminDAO.findAll().get(0);
//        Admin foundAdmin = adminDAO.findById(savedAdmin.getId());
//
//        assertThat(foundAdmin).isNotNull();
//        assertThat(foundAdmin.getAdminName()).isEqualTo("admin1");
//        assertThat(foundAdmin.getAdminPassword()).isEqualTo("password1");
//    }
//
//    @Test
//    public void testFindAll() {
//        Admin admin1 = new Admin(null, "admin1", "password1");
//        Admin admin2 = new Admin(null, "admin2", "password2");
//        adminDAO.save(admin1);
//        adminDAO.save(admin2);
//
//        List<Admin> admins = adminDAO.findAll();
//        assertThat(admins).hasSize(2);
//        assertThat(admins).extracting("adminName").containsExactlyInAnyOrder("admin1", "admin2");
//    }
//
//    @Test
//    public void testUpdate() {
//        Admin admin = new Admin(null, "admin1", "password1");
//        adminDAO.save(admin);
//
//        Admin savedAdmin = adminDAO.findAll().get(0);
//        savedAdmin.setAdminName("updatedAdmin");
//        savedAdmin.setAdminPassword("updatedPassword");
//        adminDAO.update(savedAdmin);
//
//        Admin updatedAdmin = adminDAO.findById(savedAdmin.getId());
//        assertThat(updatedAdmin.getAdminName()).isEqualTo("updatedAdmin");
//        assertThat(updatedAdmin.getAdminPassword()).isEqualTo("updatedPassword");
//    }
//
//    @Test
//    public void testDeleteById() {
//        Admin admin = new Admin(null, "admin1", "password1");
//        adminDAO.save(admin);
//
//        Admin savedAdmin = adminDAO.findAll().get(0);
//        adminDAO.deleteById(savedAdmin.getId());
//
//        List<Admin> admins = adminDAO.findAll();
//        assertThat(admins).isEmpty();
//    }
//}
