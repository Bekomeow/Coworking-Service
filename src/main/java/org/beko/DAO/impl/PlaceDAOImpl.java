package org.beko.DAO.impl;

import org.beko.DAO.PlaceDAO;
import org.beko.model.Place;
import org.beko.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaceDAOImpl implements PlaceDAO {

    @Override
    public void save(Place place) {
        String sql = "INSERT INTO coworking.\"Place\" (name, type) VALUES (?, ?)";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, place.getName());
            statement.setString(2, place.getType());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Place findById(Long id) {
        String sql = "SELECT * FROM coworking.\"Place\" WHERE id = ?";
        Place place = null;
        try (Connection connection = ConnectionManager.getConnection();
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

    @Override
    public List<Place> findAll() {
        String sql = "SELECT * FROM coworking.\"Place\"";
        List<Place> places = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
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

    @Override
    public void update(Place place) {
        String sql = "UPDATE coworking.\"Place\" SET name = ?, type = ? WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, place.getName());
            statement.setString(2, place.getType());
            statement.setLong(3, place.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM coworking.\"Place\" WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
