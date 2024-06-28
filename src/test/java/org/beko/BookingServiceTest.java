package org.beko;

import org.beko.model.Booking;
import org.beko.model.Place;
import org.beko.model.User;
import org.beko.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    private BookingService bookingService;
    private User user;
    private Place place;

    @BeforeEach
    public void setUp() {
        bookingService = new BookingService();
        user = new User("testUser", "password");
        place = new Place("1", "Test Place", "workspace");
    }

    @Test
    public void testBookPlaceSuccessfully() {
        LocalDateTime startTime = LocalDateTime.of(2024, 6, 20, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 6, 20, 11, 0);
        Booking booking = bookingService.bookPlace(user, place, startTime, endTime);
        assertNotNull(booking);
        assertEquals("testUser", booking.getUser().getUsername());
        assertEquals("Test Place", booking.getPlace().getName());
    }

    @Test
    public void testBookPlaceWithConflict() {
        LocalDateTime startTime = LocalDateTime.of(2024, 6, 20, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 6, 20, 11, 0);
        bookingService.bookPlace(user, place, startTime, endTime);

        LocalDateTime newStartTime = LocalDateTime.of(2024, 6, 20, 10, 0);
        LocalDateTime newEndTime = LocalDateTime.of(2024, 6, 20, 12, 0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.bookPlace(user, place, newStartTime, newEndTime);
        });
        assertEquals("Resource is already booked for the selected time.", exception.getMessage());
    }


    @Test
    public void testCancelBooking() {
        LocalDateTime startTime = LocalDateTime.of(2024, 6, 20, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 6, 20, 11, 0);
        Booking booking = bookingService.bookPlace(user, place, startTime, endTime);
        String bookingId = booking.getId();
        bookingService.cancelBooking(bookingId);
        assertFalse(bookingService.hasBooking(bookingId));
    }

    @Test
    public void testListBookingsByUser() {
        LocalDateTime startTime = LocalDateTime.of(2024, 6, 20, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 6, 20, 11, 0);
        bookingService.bookPlace(user, place, startTime, endTime);
        List<Booking> userBookings = bookingService.listBookingsByUser("testUser");
        assertEquals(1, userBookings.size());
    }

    @Test
    public void testListBookingsByPlace() {
        LocalDateTime startTime = LocalDateTime.of(2024, 6, 20, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 6, 20, 11, 0);
        bookingService.bookPlace(user, place, startTime, endTime);
        List<Booking> placeBookings = bookingService.listBookingsByPlace("1");
        assertEquals(1, placeBookings.size());
    }

    @Test
    public void testListBookingsByDate() {
        LocalDateTime startTime = LocalDateTime.of(2024, 6, 20, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 6, 20, 11, 0);
        bookingService.bookPlace(user, place, startTime, endTime);
        List<Booking> dateBookings = bookingService.listBookingsByDate(LocalDate.of(2024, 6, 20));
        assertEquals(1, dateBookings.size());
    }
}
