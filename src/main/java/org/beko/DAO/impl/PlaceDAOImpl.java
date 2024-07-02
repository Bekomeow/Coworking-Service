package org.beko.DAO.impl;

import lombok.RequiredArgsConstructor;
import org.beko.DAO.PlaceDAO;
import org.beko.model.Place;
import org.beko.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the PlaceDAO interface for managing Place entities in the database.
 */
@RequiredArgsConstructor
public class PlaceDAOImpl implements PlaceDAO {
    private final ConnectionManager connectionManager;
    /**
     * Saves a new Place entity to the database.
     *
     * @param place the Place entity to be saved
     */
    @Override
    public void save(Place place) {
        String sql = "INSERT INTO coworking.\"place\" (name, type) VALUES (?, ?)";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, place.getName());
            statement.setString(2, place.getType());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds a Place entity by its ID.
     *
     * @param id the ID of the Place entity to find
     * @return the found Place entity, or null if not found
     */
    @Override
    public Place findById(Long id) {
        String sql = "SELECT * FROM coworking.\"place\" WHERE id = ?";
        Place place = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                place = new Place(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return place;
    }

    /**
     * Finds all Place entities in the database.
     *
     * @return a list of all Place entities
     */
    @Override
    public List<Place> findAll() {
        String sql = "SELECT * FROM coworking.\"place\"";
        List<Place> places = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Place place = new Place(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("type"));
                places.add(place);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return places;
    }

    /**
     * Updates an existing Place entity in the database.
     *
     * @param place the Place entity with updated information
     */
    @Override
    public void update(Place place) {
        String sql = "UPDATE coworking.\"place\" SET name = ?, type = ? WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, place.getName());
            statement.setString(2, place.getType());
            statement.setLong(3, place.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a Place entity by its ID.
     *
     * @param id the ID of the Place entity to be deleted
     */
    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM coworking.\"place\" WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
