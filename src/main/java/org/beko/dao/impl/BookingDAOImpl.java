package org.beko.dao.impl;

import org.beko.dao.BookingDAO;
import org.beko.model.Booking;
import org.beko.model.Place;
import org.beko.util.ConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the BookingDAO interface for managing Booking entities in the database.
 */
@Repository
public class BookingDAOImpl implements BookingDAO {
    private final ConnectionManager connectionManager;
    private final PlaceDAOImpl PLACE_DAO;
    private final UserDAOImpl USER_DAO;

    public BookingDAOImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        PLACE_DAO = new PlaceDAOImpl(connectionManager);
        USER_DAO = new UserDAOImpl(connectionManager);
    }

    /**
     * Saves a new Booking entity to the database.
     *
     * @param booking the Booking entity to be saved
     */
    @Override
    public Booking save(Booking booking) {
        String sql = "INSERT INTO coworking.\"booking\" (user_id, place_id, start_time, end_time) VALUES (?, ?, ?, ?)";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, booking.getUser().getId());
            statement.setLong(2, booking.getPlace().getId());
            statement.setTimestamp(3, Timestamp.valueOf(booking.getStartTime()));
            statement.setTimestamp(4, Timestamp.valueOf(booking.getEndTime()));
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    booking.setId(keys.getObject(1, Long.class));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booking;
    }

    /**
     * Finds a Booking entity by its ID.
     *
     * @param id the ID of the Booking entity to find
     * @return the found Booking entity, or null if not found
     */
    @Override
    public Booking findById(Long id) {
        String sql = "SELECT * FROM coworking.\"booking\" WHERE id = ?";
        Booking booking = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                booking = new Booking(
                        resultSet.getLong("id"),
                        USER_DAO.findById(resultSet.getLong("user_id")),
                        PLACE_DAO.findById(resultSet.getLong("place_id")),
                        resultSet.getTimestamp("start_time").toLocalDateTime(),
                        resultSet.getTimestamp("end_time").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booking;
    }

    /**
     * Finds all Booking entities in the database.
     *
     * @return a list of all Booking entities
     */
    @Override
    public List<Booking> findAll() {
        String sql = "SELECT * FROM coworking.\"booking\"";
        List<Booking> bookings = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Booking booking = new Booking(
                        resultSet.getLong("id"),
                        USER_DAO.findById(resultSet.getLong("user_id")),
                        PLACE_DAO.findById(resultSet.getLong("place_id")),
                        resultSet.getTimestamp("start_time").toLocalDateTime(),
                        resultSet.getTimestamp("end_time").toLocalDateTime()
                );
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    /**
     * Updates an existing Booking entity in the database.
     *
     * @param booking the Booking entity with updated information
     */
    @Override
    public void update(Booking booking) {
        String sql = "UPDATE coworking.\"booking\" SET user_id = ?, place_id = ?, start_time = ?, end_time = ? WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, booking.getUser().getId());
            statement.setLong(2, booking.getPlace().getId());
            statement.setTimestamp(3, Timestamp.valueOf(booking.getStartTime()));
            statement.setTimestamp(4, Timestamp.valueOf(booking.getEndTime()));
            statement.setLong(5, booking.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a Booking entity by its ID.
     *
     * @param id the ID of the Booking entity to be deleted
     */
    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM coworking.\"booking\" WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds Booking entities by a user's username.
     *
     * @param username the username to search for
     * @return a list of Booking entities associated with the given username
     */
    @Override
    public List<Booking> findByUsername(String username) {
        List<Booking> bookings = findAll();
        List<Booking> userBookings = new ArrayList<>();

        for (Booking booking: bookings) {
            if (booking.getUser().getUsername().equals(username)) {
                userBookings.add(booking);
            }
        }
        return userBookings;
    }

    /**
     * Finds Booking entities by a place ID.
     *
     * @param placeName the place name to search for
     * @return a list of Booking entities associated with the given place name
     */
    @Override
    public List<Booking> findByPlaceName(String placeName) {
        List<Booking> bookings = findAll();
        List<Booking> placeBookings = new ArrayList<>();

        for (Booking booking: bookings) {
            if (booking.getPlace().getName().equals(placeName)) {
                placeBookings.add(booking);
            }
        }
        return placeBookings;
    }

    /**
     * Finds Booking entities by a specific date.
     *
     * @param date the date to search for
     * @return a list of Booking entities associated with the given date
     */
    @Override
    public List<Booking> findByDate(LocalDate date) {
        List<Booking> bookings = findAll();
        List<Booking> dateBookings = new ArrayList<>();

        for (Booking booking: bookings) {
            if (booking.getStartTime().toLocalDate().equals(date)) {
                dateBookings.add(booking);
            }
        }
        return dateBookings;
    }

    @Override
    public List<Place> findAllAvailablePlacesForDate(LocalDate date) {
        List<Booking> bookings = findAll();
        List<Place> allPlaces = PLACE_DAO.findAll();
        List<Place> bookedPlaces = new ArrayList<>();

        for (Booking booking: bookings) {
            if (booking.getStartTime().toLocalDate().equals(date)) {
                bookedPlaces.add(booking.getPlace());
            }
        }

        List<Place> freePlaces = new ArrayList<>(allPlaces);
        freePlaces.removeAll(bookedPlaces);

        return freePlaces;
    }

    @Override
    public List<Place> findAllAvailablePlaces() {
        List<Booking> bookings = findAll();
        List<Place> allPlaces = PLACE_DAO.findAll();
        List<Place> bookedPlaces = new ArrayList<>();

        for (Booking booking: bookings) {
            bookedPlaces.add(booking.getPlace());
        }

        List<Place> freePlaces = new ArrayList<>(allPlaces);
        freePlaces.removeAll(bookedPlaces);

        return freePlaces;
    }
}
