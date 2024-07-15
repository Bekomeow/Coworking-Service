//package org.beko.dao;
//
//import org.beko.dao.impl.BookingDAOImpl;
//import org.beko.dao.impl.PlaceDAOImpl;
//import org.beko.dao.impl.UserDAOImpl;
//import org.beko.containers.PostgresTestContainer;
//import org.beko.liquibase.LiquibaseDemo;
//import org.beko.model.Booking;
//import org.beko.model.Place;
//import org.beko.model.User;
//import org.beko.util.ConnectionManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.time.LocalDateTime;
//import java.util.List;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.within;
//import static java.time.temporal.ChronoUnit.MILLIS;
//
//public class BookingDAOImplTest extends PostgresTestContainer {
//    private BookingDAOImpl bookingDAO;
//    private UserDAOImpl userDAO;
//    private PlaceDAOImpl placeDAO;
//
//    @BeforeEach
//    public void setUp() {
//        ConnectionManager connectionManager = new ConnectionManager();
//
//        String changeLogFile = "changeLogFile = db/changelog/main-changelog.xml;\n";
//        String schemaName = "db/changelog/main-changelog.xml";
//
//        LiquibaseDemo liquibaseDemo = new LiquibaseDemo(connectionManager.getConnection(
//                container.getJdbcUrl(),
//                container.getUsername(),
//                container.getPassword(),
//                "org.postgresql.Driver"
//        ), changeLogFile, schemaName);
//
//        liquibaseDemo.runMigrations();
//
//        userDAO = new UserDAOImpl(connectionManager);
//        placeDAO = new PlaceDAOImpl(connectionManager);
//        bookingDAO = new BookingDAOImpl(connectionManager);
//
//        clearBookingTable(connectionManager);
//        clearUserTable(connectionManager);
//        clearPlaceTable(connectionManager);
//    }
//
//    private void clearBookingTable(ConnectionManager connectionManager) {
//        String sql = "DELETE FROM coworking.\"booking\"";
//        try (var connection = connectionManager.getConnection();
//             Statement statement = connection.createStatement()) {
//            statement.executeUpdate(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void clearUserTable(ConnectionManager connectionManager) {
//        String sql = "DELETE FROM coworking.\"user\"";
//        try (var connection = connectionManager.getConnection();
//             Statement statement = connection.createStatement()) {
//            statement.executeUpdate(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
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
//    @DisplayName("Save Booking and Verify")
//    public void testSave() {
//        User user = User.builder()
//                .username("john_doe")
//                .password("password123")
//                .build();
//
//        Place place = Place.builder()
//                .name("Meeting Room")
//                .type("conference-room")
//                .build();
//
//        userDAO.save(user);
//        placeDAO.save(place);
//
//        User savedUser = userDAO.findAll().get(0);
//        Place savedPlace = placeDAO.findAll().get(0);
//
//        Booking booking = Booking.builder()
//                .user(savedUser)
//                .place(savedPlace)
//                .startTime(LocalDateTime.now())
//                .endTime(LocalDateTime.now().plusHours(1))
//                .build();
//
//        bookingDAO.save(booking);
//
//        List<Booking> bookings = bookingDAO.findAll();
//        assertThat(bookings).hasSize(1);
//        Booking savedBooking = bookings.get(0);
//        assertThat(savedBooking.getUser().getId()).isEqualTo(savedUser.getId());
//        assertThat(savedBooking.getPlace().getId()).isEqualTo(savedPlace.getId());
//    }
//
//    @Test
//    @DisplayName("Find Booking by ID")
//    public void testFindById() {
//        User user = User.builder()
//                .username("john_doe")
//                .password("password123")
//                .build();
//
//        Place place = Place.builder()
//                .name("Meeting Room")
//                .type("conference-room")
//                .build();
//
//        userDAO.save(user);
//        placeDAO.save(place);
//
//        User savedUser = userDAO.findAll().get(0);
//        Place savedPlace = placeDAO.findAll().get(0);
//
//        Booking booking = Booking.builder()
//                .user(savedUser)
//                .place(savedPlace)
//                .startTime(LocalDateTime.now())
//                .endTime(LocalDateTime.now().plusHours(1))
//                .build();
//
//        bookingDAO.save(booking);
//
//        Booking savedBooking = bookingDAO.findAll().get(0);
//        Booking foundBooking = bookingDAO.findById(savedBooking.getId());
//
//        assertThat(foundBooking).isNotNull();
//        assertThat(foundBooking.getId()).isEqualTo(savedBooking.getId());
//    }
//
//    @Test
//    @DisplayName("Find All Bookings")
//    public void testUpdate() {
//        User user = User.builder()
//                .username("john_doe")
//                .password("password123")
//                .build();
//
//        Place place = Place.builder()
//                .name("Meeting Room")
//                .type("conference-room")
//                .build();
//
//        userDAO.save(user);
//        placeDAO.save(place);
//
//        User savedUser = userDAO.findAll().get(0);
//        Place savedPlace = placeDAO.findAll().get(0);
//
//        Booking booking = Booking.builder()
//                .user(savedUser)
//                .place(savedPlace)
//                .startTime(LocalDateTime.now())
//                .endTime(LocalDateTime.now().plusHours(1))
//                .build();
//
//        bookingDAO.save(booking);
//
//        Booking savedBooking = bookingDAO.findAll().get(0);
//        savedBooking.setStartTime(LocalDateTime.now().plusHours(2));
//        bookingDAO.update(savedBooking);
//
//        Booking updatedBooking = bookingDAO.findById(savedBooking.getId());
//        assertThat(updatedBooking).isNotNull();
//        assertThat(updatedBooking.getStartTime())
//                .isCloseTo(savedBooking.getStartTime(), within(2, MILLIS));
//    }
//
//    @Test
//    @DisplayName("Delete Booking by ID")
//    public void testDeleteById() {
//        User user = User.builder()
//                .username("john_doe")
//                .password("password123")
//                .build();
//
//        Place place = Place.builder()
//                .name("Meeting Room")
//                .type("conference-room")
//                .build();
//
//        userDAO.save(user);
//        placeDAO.save(place);
//
//        User savedUser = userDAO.findAll().get(0);
//        Place savedPlace = placeDAO.findAll().get(0);
//
//        Booking booking = Booking.builder()
//                .user(savedUser)
//                .place(savedPlace)
//                .startTime(LocalDateTime.now())
//                .endTime(LocalDateTime.now().plusHours(1))
//                .build();
//
//        bookingDAO.save(booking);
//
//        List<Booking> bookingsBeforeDeletion = bookingDAO.findAll();
//        assertThat(bookingsBeforeDeletion).hasSize(1);
//
//        Booking savedBooking = bookingsBeforeDeletion.get(0);
//        bookingDAO.deleteById(savedBooking.getId());
//
//        List<Booking> bookingsAfterDeletion = bookingDAO.findAll();
//        assertThat(bookingsAfterDeletion).isEmpty();
//    }
//
//    @Test
//    @DisplayName("Find Booking by username")
//    public void testFindByUsername() {
//        User user = User.builder()
//                .username("john_doe")
//                .password("password123")
//                .build();
//
//        Place place = Place.builder()
//                .name("Meeting Room")
//                .type("conference-room")
//                .build();
//
//        userDAO.save(user);
//        placeDAO.save(place);
//
//        User savedUser = userDAO.findAll().get(0);
//        Place savedPlace = placeDAO.findAll().get(0);
//
//        Booking booking1 = Booking.builder()
//                .user(savedUser)
//                .place(savedPlace)
//                .startTime(LocalDateTime.now())
//                .endTime(LocalDateTime.now().plusHours(1))
//                .build();
//
//        Booking booking2 = Booking.builder()
//                .user(savedUser)
//                .place(savedPlace)
//                .startTime(LocalDateTime.now().plusDays(1))
//                .endTime(LocalDateTime.now().plusDays(1).plusHours(1))
//                .build();
//
//        bookingDAO.save(booking1);
//        bookingDAO.save(booking2);
//
//        List<Booking> userBookings = bookingDAO.findByUsername(savedUser.getUsername());
//        assertThat(userBookings).hasSize(2);
//    }
//
//    @Test
//    @DisplayName("Find Booking by place")
//    public void testFindByPlace() {
//        User user = User.builder()
//                .username("john_doe")
//                .password("password123")
//                .build();
//
//        Place place1 = Place.builder()
//                .name("Meeting Room")
//                .type("conference-room")
//                .build();
//
//        Place place2 = Place.builder()
//                .name("Office Space")
//                .type("desk")
//                .build();
//
//        userDAO.save(user);
//        placeDAO.save(place1);
//        placeDAO.save(place2);
//
//        User savedUser = userDAO.findAll().get(0);
//        Place savedPlace1 = placeDAO.findAll().get(0);
//        Place savedPlace2 = placeDAO.findAll().get(1);
//
//        Booking booking1 = Booking.builder()
//                .user(savedUser)
//                .place(savedPlace1)
//                .startTime(LocalDateTime.now())
//                .endTime(LocalDateTime.now().plusHours(1))
//                .build();
//
//        Booking booking2 = Booking.builder()
//                .user(savedUser)
//                .place(savedPlace2)
//                .startTime(LocalDateTime.now().plusDays(1))
//                .endTime(LocalDateTime.now().plusDays(1).plusHours(1))
//                .build();
//
//        bookingDAO.save(booking1);
//        bookingDAO.save(booking2);
//
//        List<Booking> placeBookings = bookingDAO.findByPlaceName(savedPlace1.getName());
//        assertThat(placeBookings).hasSize(1);
//        assertThat(placeBookings.get(0).getPlace().getId()).isEqualTo(savedPlace1.getId());
//    }
//
//    @Test
//    @DisplayName("Find Booking by date")
//    public void testFindByDate() {
//        User user = User.builder()
//                .username("john_doe")
//                .password("password123")
//                .build();
//
//        Place place = Place.builder()
//                .name("Meeting Room")
//                .type("conference-room")
//                .build();
//
//        userDAO.save(user);
//        placeDAO.save(place);
//
//        User savedUser = userDAO.findAll().get(0);
//        Place savedPlace = placeDAO.findAll().get(0);
//
//        LocalDateTime today = LocalDateTime.now();
//        LocalDateTime tomorrow = today.plusDays(1);
//
//        Booking booking1 = Booking.builder()
//                .user(savedUser)
//                .place(savedPlace)
//                .startTime(today)
//                .endTime(today.plusHours(1))
//                .build();
//
//        Booking booking2 = Booking.builder()
//                .user(savedUser)
//                .place(savedPlace)
//                .startTime(tomorrow)
//                .endTime(tomorrow.plusHours(1))
//                .build();
//
//        bookingDAO.save(booking1);
//        bookingDAO.save(booking2);
//
//        List<Booking> dateBookings = bookingDAO.findByDate(today.toLocalDate());
//        assertThat(dateBookings).hasSize(1);
//        assertThat(dateBookings.get(0).getStartTime().toLocalDate()).isEqualTo(today.toLocalDate());
//    }
//}
//
