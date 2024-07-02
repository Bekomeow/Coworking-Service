package org.beko.DAO;

import org.beko.DAO.impl.BookingDAOImpl;
import org.beko.DAO.impl.PlaceDAOImpl;
import org.beko.DAO.impl.UserDAOImpl;
import org.beko.containers.PostgresTestContainer;
import org.beko.liquibase.LiquibaseDemo;
import org.beko.model.Booking;
import org.beko.model.Place;
import org.beko.model.User;
import org.beko.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static java.time.temporal.ChronoUnit.MILLIS;

public class BookingDAOImplTest extends PostgresTestContainer {
    private BookingDAOImpl bookingDAO;
    private UserDAOImpl userDAO;
    private PlaceDAOImpl placeDAO;

    @BeforeEach
    public void setUp() {
        ConnectionManager connectionManager = new ConnectionManager(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        LiquibaseDemo liquibaseTest = LiquibaseDemo.getInstance();
        liquibaseTest.runMigrations(connectionManager.getConnection());

        userDAO = new UserDAOImpl(connectionManager);
        placeDAO = new PlaceDAOImpl(connectionManager);
        bookingDAO = new BookingDAOImpl(connectionManager);

        clearBookingTable(connectionManager);
        clearUserTable(connectionManager);
        clearPlaceTable(connectionManager);
    }

    private void clearBookingTable(ConnectionManager connectionManager) {
        String sql = "DELETE FROM coworking.\"booking\"";
        try (var connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearUserTable(ConnectionManager connectionManager) {
        String sql = "DELETE FROM coworking.\"user\"";
        try (var connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    public void testSave() {
        User user = new User("john_doe", "password123");
        Place place = new Place("Meeting Room", "conference");
        userDAO.save(user);
        placeDAO.save(place);

        User savedUser = userDAO.findAll().get(0);
        Place savedPlace = placeDAO.findAll().get(0);

        Booking booking = new Booking(savedUser, savedPlace, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        bookingDAO.save(booking);

        List<Booking> bookings = bookingDAO.findAll();
        assertThat(bookings).hasSize(1);
        Booking savedBooking = bookings.get(0);
        assertThat(savedBooking.getUser().getId()).isEqualTo(savedUser.getId());
        assertThat(savedBooking.getPlace().getId()).isEqualTo(savedPlace.getId());
    }

    @Test
    public void testFindById() {
        User user = new User("john_doe", "password123");
        Place place = new Place("Meeting Room", "conference");
        userDAO.save(user);
        placeDAO.save(place);

        User savedUser = userDAO.findAll().get(0);
        Place savedPlace = placeDAO.findAll().get(0);

        Booking booking = new Booking(savedUser, savedPlace, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        bookingDAO.save(booking);

        Booking savedBooking = bookingDAO.findAll().get(0);
        Booking foundBooking = bookingDAO.findById(savedBooking.getId());

        assertThat(foundBooking).isNotNull();
        assertThat(foundBooking.getId()).isEqualTo(savedBooking.getId());
    }

    @Test
    public void testUpdate() {
        User user = new User("john_doe", "password123");
        Place place = new Place("Meeting Room", "conference");
        userDAO.save(user);
        placeDAO.save(place);

        User savedUser = userDAO.findAll().get(0);
        Place savedPlace = placeDAO.findAll().get(0);

        Booking booking = new Booking(savedUser, savedPlace, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        bookingDAO.save(booking);

        Booking savedBooking = bookingDAO.findAll().get(0);
        savedBooking.setStartTime(LocalDateTime.now().plusHours(2));
        bookingDAO.update(savedBooking);

        Booking updatedBooking = bookingDAO.findById(savedBooking.getId());
        assertThat(updatedBooking).isNotNull();
        assertThat(updatedBooking.getStartTime())
                .isCloseTo(savedBooking.getStartTime(), within(2, MILLIS));
    }

    @Test
    public void testDeleteById() {
        User user = new User("john_doe", "password123");
        Place place = new Place("Meeting Room", "conference");
        userDAO.save(user);
        placeDAO.save(place);

        User savedUser = userDAO.findAll().get(0);
        Place savedPlace = placeDAO.findAll().get(0);

        Booking booking = new Booking(savedUser, savedPlace, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        bookingDAO.save(booking);

        List<Booking> bookingsBeforeDeletion = bookingDAO.findAll();
        assertThat(bookingsBeforeDeletion).hasSize(1);

        Booking savedBooking = bookingsBeforeDeletion.get(0);
        bookingDAO.deleteById(savedBooking.getId());

        List<Booking> bookingsAfterDeletion = bookingDAO.findAll();
        assertThat(bookingsAfterDeletion).isEmpty();
    }

    @Test
    public void testFindByUsername() {
        User user = new User("john_doe", "password123");
        Place place = new Place("Meeting Room", "conference");
        userDAO.save(user);
        placeDAO.save(place);

        User savedUser = userDAO.findAll().get(0);
        Place savedPlace = placeDAO.findAll().get(0);

        Booking booking1 = new Booking(savedUser, savedPlace, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        Booking booking2 = new Booking(savedUser, savedPlace, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1));
        bookingDAO.save(booking1);
        bookingDAO.save(booking2);

        List<Booking> userBookings = bookingDAO.findByUsername(savedUser.getUsername());
        assertThat(userBookings).hasSize(2);
    }

    @Test
    public void testFindByPlaceId() {
        User user = new User("john_doe", "password123");
        Place place1 = new Place("Meeting Room", "conference");
        Place place2 = new Place("Office Space", "desk");
        userDAO.save(user);
        placeDAO.save(place1);
        placeDAO.save(place2);

        User savedUser = userDAO.findAll().get(0);
        Place savedPlace1 = placeDAO.findAll().get(0);
        Place savedPlace2 = placeDAO.findAll().get(1);

        Booking booking1 = new Booking(savedUser, savedPlace1, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        Booking booking2 = new Booking(savedUser, savedPlace2, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1));
        bookingDAO.save(booking1);
        bookingDAO.save(booking2);

        List<Booking> placeBookings = bookingDAO.findByPlaceId(savedPlace1.getId());
        assertThat(placeBookings).hasSize(1);
        assertThat(placeBookings.get(0).getPlace().getId()).isEqualTo(savedPlace1.getId());
    }

    @Test
    public void testFindByDate() {
        User user = new User("john_doe", "password123");
        Place place = new Place("Meeting Room", "conference");
        userDAO.save(user);
        placeDAO.save(place);

        User savedUser = userDAO.findAll().get(0);
        Place savedPlace = placeDAO.findAll().get(0);

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime tomorrow = today.plusDays(1);

        Booking booking1 = new Booking(savedUser, savedPlace, today, today.plusHours(1));
        Booking booking2 = new Booking(savedUser, savedPlace, tomorrow, tomorrow.plusHours(1));
        bookingDAO.save(booking1);
        bookingDAO.save(booking2);

        List<Booking> dateBookings = bookingDAO.findByDate(today.toLocalDate());
        assertThat(dateBookings).hasSize(1);
        assertThat(dateBookings.get(0).getStartTime().toLocalDate()).isEqualTo(today.toLocalDate());
    }
}

