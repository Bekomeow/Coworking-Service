package org.beko.service;

import org.beko.DAO.impl.PlaceDAOImpl;
import org.beko.containers.PostgresTestContainer;
import org.beko.liquibase.LiquibaseDemo;
import org.beko.model.Place;
import org.beko.service.impl.PlaceServiceImpl;
import org.beko.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class PlaceServiceImplTest extends PostgresTestContainer {
    private PlaceServiceImpl placeService;
    private PlaceDAOImpl placeDAO;
    private ConnectionManager connectionManager;

    @BeforeEach
    public void setUp() {
        connectionManager = new ConnectionManager(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        LiquibaseDemo liquibaseTest = LiquibaseDemo.getInstance();
        liquibaseTest.runMigrations(connectionManager.getConnection());

        placeDAO = new PlaceDAOImpl(connectionManager);
        placeService = new PlaceServiceImpl(connectionManager);
        clearPlaceTable();
        resetSequence();
    }

    private void clearPlaceTable() {
        String sql = "DELETE FROM coworking.\"place\"";
        try (var connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetSequence() {
        String sql = "ALTER SEQUENCE coworking.\"place_id_seq\" RESTART WITH 1";
        try (var connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Test
    void testAddPlace() {
        Place place = placeService.addPlace("Conference Room", "conference");

        assertThat(place).isNotNull();
        assertThat(place.getName()).isEqualTo("Conference Room");
        assertThat(place.getType()).isEqualTo("conference");

        List<Place> places = placeService.listPlaces();
        assertThat(places).hasSize(1);
        assertThat(places.get(0).getName()).isEqualTo("Conference Room");
        assertThat(places.get(0).getType()).isEqualTo("conference");
    }

    @Test
    void testUpdatePlace() {
        Place place = placeService.addPlace("Old Name", "workspace");
        placeService.updatePlace(1L, "New Name", "conference");

        Place updatedPlace = placeService.getPlaceById(1L).orElse(null);

        assertThat(updatedPlace).isNotNull();
        assertThat(updatedPlace.getName()).isEqualTo("New Name");
        assertThat(updatedPlace.getType()).isEqualTo("conference");
    }

    @Test
    void testHasPlace() {
        Place place = placeService.addPlace("Workspace", "workspace");

        boolean exists = placeService.hasPlace(1L);
        boolean notExists = placeService.hasPlace(999L);

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void testDeletePlace() {
        Place place = placeService.addPlace("To Be Deleted", "workspace");

        placeService.deletePlace(1L);

        boolean exists = placeService.hasPlace(1L);
        assertThat(exists).isFalse();
    }

    @Test
    void testListPlaces() {
        placeService.addPlace("Place 1", "workspace");
        placeService.addPlace("Place 2", "conference");

        List<Place> places = placeService.listPlaces();
        assertThat(places).hasSize(2);
        assertThat(places).extracting("name").containsExactlyInAnyOrder("Place 1", "Place 2");
    }

    @Test
    void testGetPlaceById() {
        Place place = placeService.addPlace("Specific Place", "workspace");

        System.out.println(placeDAO.findAll());

        Place foundPlace = placeService.getPlaceById(1L).orElse(null);

        assertThat(foundPlace).isNotNull();
        assertThat(foundPlace.getName()).isEqualTo("Specific Place");
        assertThat(foundPlace.getType()).isEqualTo("workspace");
    }
}
