//package org.beko.dao;
//
//import org.beko.dao.impl.PlaceDAOImpl;
//import org.beko.containers.PostgresTestContainer;
//import org.beko.liquibase.LiquibaseDemo;
//import org.beko.model.Place;
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
//public class PlaceDAOImplTest extends PostgresTestContainer {
//    private PlaceDAOImpl placeDAO;
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
//        placeDAO = new PlaceDAOImpl(connectionManager);
//        clearPlaceTable(connectionManager);
//    }
//
//    private void clearPlaceTable(ConnectionManager connectionManager) {
//        String sql = "DELETE FROM coworking.\"place\"";
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
//        Place place = new Place("Meeting Room", "conference");
//        placeDAO.save(place);
//
//        List<Place> places = placeDAO.findAll();
//        assertThat(places).hasSize(1);
//        Place savedPlace = places.get(0);
//        assertThat(savedPlace.getName()).isEqualTo("Meeting Room");
//        assertThat(savedPlace.getType()).isEqualTo("conference");
//    }
//
//    @Test
//    public void testFindById() {
//        Place place = new Place("Workspace", "workspace");
//        placeDAO.save(place);
//
//        Place savedPlace = placeDAO.findAll().get(0);
//        Place foundPlace = placeDAO.findById(savedPlace.getId());
//
//        assertThat(foundPlace).isNotNull();
//        assertThat(foundPlace.getId()).isEqualTo(savedPlace.getId());
//        assertThat(foundPlace.getName()).isEqualTo("Workspace");
//        assertThat(foundPlace.getType()).isEqualTo("workspace");
//    }
//
//    @Test
//    public void testFindAll() {
//        Place place1 = new Place("Workspace 1", "workspace");
//        Place place2 = new Place("Workspace 2", "workspace");
//        placeDAO.save(place1);
//        placeDAO.save(place2);
//
//        List<Place> places = placeDAO.findAll();
//        assertThat(places).hasSize(2);
//        assertThat(places).extracting("name").containsExactlyInAnyOrder("Workspace 1", "Workspace 2");
//    }
//
//    @Test
//    public void testUpdate() {
//        Place place = new Place("Meeting Room", "conference");
//        placeDAO.save(place);
//
//        Place savedPlace = placeDAO.findAll().get(0);
//        savedPlace.setName("Updated Room");
//        placeDAO.update(savedPlace);
//
//        Place updatedPlace = placeDAO.findById(savedPlace.getId());
//        assertThat(updatedPlace.getName()).isEqualTo("Updated Room");
//    }
//
//    @Test
//    public void testDeleteById() {
//        Place place = new Place("Meeting Room", "conference");
//        placeDAO.save(place);
//
//        Place savedPlace = placeDAO.findAll().get(0);
//        placeDAO.deleteById(savedPlace.getId());
//
//        List<Place> places = placeDAO.findAll();
//        assertThat(places).isEmpty();
//    }
//}
