package org.beko.DAO.impl;

import org.beko.DAO.BookingDAO;
import org.beko.model.Booking;
import org.beko.util.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {
    private static final PlaceDAOImpl PLACE_DAO = new PlaceDAOImpl();
    private static final UserDAOImpl USER_DAO = new UserDAOImpl();

    @Override
    public void save(Booking booking) {
        String sql = "INSERT INTO coworking.\"Booking\" (user_id, place_id, start_time, end_time) VALUES (?, ?, ?, ?)";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, booking.getUser().getId());
            statement.setLong(2, booking.getPlace().getId());
            statement.setTimestamp(3, Timestamp.valueOf(booking.getStartTime()));
            statement.setTimestamp(4, Timestamp.valueOf(booking.getEndTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Booking findById(Long id) {
        String sql = "SELECT * FROM coworking.\"Booking\" WHERE id = ?";
        Booking booking = null;
        try (Connection connection = ConnectionManager.getConnection();
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

    @Override
    public List<Booking> findAll() {
        String sql = "SELECT * FROM coworking.\"Booking\"";
        List<Booking> bookings = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
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

    @Override
    public void update(Booking booking) {
        String sql = "UPDATE coworking.\"Booking\" SET user_id = ?, place_id = ?, start_time = ?, end_time = ? WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
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

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM coworking.\"Booking\" WHERE id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    @Override
    public List<Booking> findByPlaceId(Long placeId) {
        List<Booking> bookings = findAll();
        List<Booking> placeBookings = new ArrayList<>();

        for (Booking booking: bookings) {
            if (booking.getPlace().getId().equals(placeId)) {
                placeBookings.add(booking);
            }
        }
        return placeBookings;
    }

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
}
