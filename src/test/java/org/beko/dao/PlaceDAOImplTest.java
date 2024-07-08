package org.beko.dao;

import org.beko.dao.impl.PlaceDAOImpl;
import org.beko.containers.PostgresTestContainer;
import org.beko.liquibase.LiquibaseDemo;
import org.beko.model.Place;
import org.beko.util.ConnectionManager;
import org.beko.util.PropertiesUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PlaceDAOImplTest extends PostgresTestContainer {
    private PlaceDAOImpl placeDAO;

    @BeforeEach
    public void setUp() {
        ConnectionManager connectionManager = new ConnectionManager(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );

        String changeLogFile = PropertiesUtil.get("liquibase.change-log");
        String schemaName = PropertiesUtil.get("liquibase.liquibase-schema");

        LiquibaseDemo liquibaseDemo = new LiquibaseDemo(connectionManager.getConnection(), changeLogFile, schemaName);
        liquibaseDemo.runMigrations();


        placeDAO = new PlaceDAOImpl(connectionManager);
        clearPlaceTable(connectionManager);
    }

    private void clearPlaceTable(ConnectionManager connectionManager) {
        String sql = "DELETE FROM coworking.\"place\"";
        try (var connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Save Place and Verify")
    public void testSave() {
        Place place = Place.builder()
                .name("Meeting Room")
                .type("conference-room")
                .build();

        placeDAO.save(place);

        List<Place> places = placeDAO.findAll();
        assertThat(places).hasSize(1);
        Place savedPlace = places.get(0);
        assertThat(savedPlace.getName()).isEqualTo("Meeting Room");
        assertThat(savedPlace.getType()).isEqualTo("conference-room");
    }

    @Test
    @DisplayName("Find Place by ID")
    public void testFindById() {
        Place place = Place.builder()
                .name("Meeting Room")
                .type("conference-room")
                .build();

        placeDAO.save(place);

        Place savedPlace = placeDAO.findAll().get(0);
        Place foundPlace = placeDAO.findById(savedPlace.getId());

        assertThat(foundPlace).isNotNull();
        assertThat(foundPlace.getId()).isEqualTo(savedPlace.getId());
        assertThat(foundPlace.getName()).isEqualTo("Meeting Room");
        assertThat(foundPlace.getType()).isEqualTo("conference-room");
    }

    @Test
    @DisplayName("Find All Places")
    public void testFindAll() {
        Place place1 = Place.builder()
                .name("Workspace 1")
                .type("workspace")
                .build();

        Place place2 = Place.builder()
                .name("Workspace 2")
                .type("workspace")
                .build();

        placeDAO.save(place1);
        placeDAO.save(place2);

        List<Place> places = placeDAO.findAll();
        assertThat(places).hasSize(2);
        assertThat(places).extracting("name").containsExactlyInAnyOrder("Workspace 1", "Workspace 2");
    }

    @Test
    @DisplayName("Update Place and Verify")
    public void testUpdate() {
        Place place = Place.builder()
                .name("Meeting Room")
                .type("conference-room")
                .build();

        placeDAO.save(place);

        Place savedPlace = placeDAO.findAll().get(0);
        savedPlace.setName("Updated Room");
        placeDAO.update(savedPlace);

        Place updatedPlace = placeDAO.findById(savedPlace.getId());
        assertThat(updatedPlace.getName()).isEqualTo("Updated Room");
    }

    @Test
    @DisplayName("Delete Place by ID")
    public void testDeleteById() {
        Place place = Place.builder()
                .name("Meeting Room")
                .type("conference-room")
                .build();

        placeDAO.save(place);

        Place savedPlace = placeDAO.findAll().get(0);
        placeDAO.deleteById(savedPlace.getId());

        List<Place> places = placeDAO.findAll();
        assertThat(places).isEmpty();
    }
}
