package org.beko.service;

import org.beko.dao.impl.BookingDAOImpl;
import org.beko.containers.PostgresTestContainer;
import org.beko.dao.impl.PlaceDAOImpl;
import org.beko.dao.impl.UserDAOImpl;
import org.beko.liquibase.LiquibaseDemo;
import org.beko.model.Booking;
import org.beko.model.Place;
import org.beko.model.User;
import org.beko.security.JwtTokenUtils;
import org.beko.service.impl.BookingServiceImpl;
import org.beko.service.impl.PlaceServiceImpl;
import org.beko.service.impl.SecurityServiceImpl;
import org.beko.util.ConnectionManager;
import org.beko.util.PropertiesUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class BookingServiceImplTest extends PostgresTestContainer {
    private BookingServiceImpl bookingService;
    private SecurityServiceImpl securityService;
    private PlaceServiceImpl placeService;
    private BookingDAOImpl bookingDAO;
    private ConnectionManager connectionManager;

    @BeforeEach
    public void setUp() {
        connectionManager = new ConnectionManager(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );

        String changeLogFile = PropertiesUtil.get("liquibase.change-log");
        String schemaName = PropertiesUtil.get("liquibase.liquibase-schema");

        LiquibaseDemo liquibaseDemo = new LiquibaseDemo(connectionManager.getConnection(), changeLogFile, schemaName);
        liquibaseDemo.runMigrations();

        bookingDAO = new BookingDAOImpl(connectionManager);
        PlaceDAOImpl placeDAO = new PlaceDAOImpl(connectionManager);
        UserDAOImpl userDAO = new UserDAOImpl(connectionManager);

        JwtTokenUtils jwtTokenUtils = mock(JwtTokenUtils.class);

        bookingService = new BookingServiceImpl(bookingDAO);
        securityService = new SecurityServiceImpl(userDAO, jwtTokenUtils);
        placeService = new PlaceServiceImpl(placeDAO);

        clearBookingTable();
        resetSequence();
    }

    private void clearBookingTable() {
        String sql = "DELETE FROM coworking.\"booking\";" +
                "DELETE FROM coworking.\"user\";" +
                "DELETE FROM coworking.\"place\"";
        try (var connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetSequence() {
        String sql = "ALTER SEQUENCE coworking.\"booking_id_seq\" RESTART WITH 1;" +
                "ALTER SEQUENCE coworking.\"user_id_seq\" RESTART WITH 1;" +
                "ALTER SEQUENCE coworking.\"place_id_seq\" RESTART WITH 1";
        try (var connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testBookPlace() {
        User user = User.builder()
                .id(1L)
                .username("user1")
                .password("password")
                .build();
        Place place = Place.builder()
                .id(1L)
                .name("Conference Room")
                .type("conference")
                .build();

        securityService.register(user.getUsername(), user.getPassword());
        placeService.addPlace(place.getName(), place.getType());

        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(2);

        Booking booking = bookingService.bookPlace(user, place, startTime, endTime);

        assertThat(booking).isNotNull();
        assertThat(booking.getUser()).isEqualTo(user);
        assertThat(booking.getPlace()).isEqualTo(place);
        assertThat(booking.getStartTime()).isEqualTo(startTime);
        assertThat(booking.getEndTime()).isEqualTo(endTime);
    }

    @Test
    void testBookPlaceWithInvalidTime() {
        User user = User.builder()
                .id(1L)
                .username("user1")
                .password("password")
                .build();
        Place place = Place.builder()
                .id(1L)
                .name("Conference Room")
                .type("conference")
                .build();

        securityService.register(user.getUsername(), user.getPassword());
        placeService.addPlace(place.getName(), place.getType());

        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.minusHours(2);

        assertThatThrownBy(() -> bookingService.bookPlace(user, place, startTime, endTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Start time must be before end time.");
    }

    @Test
    void testCancelBooking() {
        User user = User.builder()
                .id(1L)
                .username("user1")
                .password("password")
                .build();
        Place place = Place.builder()
                .id(1L)
                .name("Conference Room")
                .type("conference")
                .build();

        securityService.register(user.getUsername(), user.getPassword());
        placeService.addPlace(place.getName(), place.getType());

        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(2);
        Booking booking = bookingService.bookPlace(user, place, startTime, endTime);

        bookingService.cancelBooking(1L);

        boolean exists = bookingService.hasBooking(1L);
        assertThat(exists).isFalse();
    }

    @Test
    void testListBookings() {
        User user = User.builder()
                .id(1L)
                .username("user1")
                .password("password")
                .build();
        Place place = Place.builder()
                .id(1L)
                .name("Conference Room")
                .type("conference")
                .build();

        securityService.register(user.getUsername(), user.getPassword());
        placeService.addPlace(place.getName(), place.getType());

        LocalDateTime startTime1 = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime1 = startTime1.plusHours(2);
        LocalDateTime startTime2 = LocalDateTime.now().plusDays(2);
        LocalDateTime endTime2 = startTime2.plusHours(2);

        bookingService.bookPlace(user, place, startTime1, endTime1);
        bookingService.bookPlace(user, place, startTime2, endTime2);

        List<Booking> bookings = bookingDAO.findAll();
        System.out.println(bookings);
        assertThat(bookings).hasSize(2);
    }

    @Test
    void testListBookingsByUser() {
        User user1 = User.builder()
                .id(1L)
                .username("user1")
                .password("password")
                .build();
        User user2 = User.builder()
                .id(2L)
                .username("user2")
                .password("password")
                .build();
        Place place = Place.builder()
                .id(1L)
                .name("Conference Room")
                .type("conference")
                .build();

        securityService.register(user1.getUsername(), user1.getPassword());
        securityService.register(user2.getUsername(), user2.getPassword());
        placeService.addPlace(place.getName(), place.getType());

        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(2);

        bookingService.bookPlace(user1, place, startTime, endTime);
        bookingService.bookPlace(user2, place, startTime.plusDays(1), endTime.plusDays(1));

        List<Booking> user1Bookings = bookingService.listBookingsByUser("user1");
        assertThat(user1Bookings).hasSize(1);
        assertThat(user1Bookings.get(0).getUser().getUsername()).isEqualTo("user1");

        List<Booking> user2Bookings = bookingService.listBookingsByUser("user2");
        assertThat(user2Bookings).hasSize(1);
        assertThat(user2Bookings.get(0).getUser().getUsername()).isEqualTo("user2");
    }

    @Test
    void testListBookingsByPlace() {
        User user = User.builder()
                .id(1L)
                .username("user1")
                .password("password")
                .build();
        Place place1 = Place.builder()
                .id(1L)
                .name("Conference Room")
                .type("conference")
                .build();
        Place place2 = Place.builder()
                .id(2L)
                .name("Meeting Room")
                .type("meeting")
                .build();

        securityService.register(user.getUsername(), user.getPassword());
        placeService.addPlace(place1.getName(), place1.getType());
        placeService.addPlace(place2.getName(), place2.getType());

        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(2);

        bookingService.bookPlace(user, place1, startTime, endTime);
        bookingService.bookPlace(user, place2, startTime.plusDays(1), endTime.plusDays(1));

        List<Booking> place1Bookings = bookingService.listBookingsByPlace(place1.getName());
        assertThat(place1Bookings).hasSize(1);
        assertThat(place1Bookings.get(0).getPlace().getName()).isEqualTo("Conference Room");

        List<Booking> place2Bookings = bookingService.listBookingsByPlace(place2.getName());
        assertThat(place2Bookings).hasSize(1);
        assertThat(place2Bookings.get(0).getPlace().getName()).isEqualTo("Meeting Room");
    }

    @Test
    void testListBookingsByDate() {
        User user = User.builder()
                .id(1L)
                .username("user1")
                .password("password")
                .build();
        Place place = Place.builder()
                .id(1L)
                .name("Conference Room")
                .type("conference")
                .build();

        securityService.register(user.getUsername(), user.getPassword());
        placeService.addPlace(place.getName(), place.getType());

        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(2);

        bookingService.bookPlace(user, place, startTime, endTime);

        List<Booking> bookings = bookingService.listBookingsByDate(LocalDate.now().plusDays(1));
        assertThat(bookings).hasSize(1);
    }

    @Test
    void testHasBooking() {
        User user = User.builder()
                .id(1L)
                .username("user1")
                .password("password")
                .build();
        Place place = Place.builder()
                .id(1L)
                .name("Conference Room")
                .type("conference")
                .build();

        securityService.register(user.getUsername(), user.getPassword());
        placeService.addPlace(place.getName(), place.getType());

        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(2);
        Booking booking = bookingService.bookPlace(user, place, startTime, endTime);

        boolean exists = bookingService.hasBooking(1L);
        boolean notExists = bookingService.hasBooking(999L);

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
