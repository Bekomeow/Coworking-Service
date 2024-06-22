package org.beko;

import org.beko.Entity.Booking;
import org.beko.Entity.Place;
import org.beko.Entity.User;
import org.beko.Service.BookingService;
import org.beko.Service.PlaceService;
import org.beko.Service.UserService;
import org.beko.Util.ScannerWrapper;
import org.beko.Handler.UserHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserHandlerTest {

    private ScannerWrapper scanner;
    private UserService userService;
    private PlaceService placeService;
    private BookingService bookingService;
    private UserHandler userHandler;
    private User user;

    @BeforeEach
    public void setUp() {
        scanner = mock(ScannerWrapper.class);
        userService = mock(UserService.class);
        placeService = mock(PlaceService.class);
        bookingService = mock(BookingService.class);
        userHandler = new UserHandler(scanner, userService, placeService, bookingService);
        user = mock(User.class);
    }

    @Test
    public void testViewPlaces() {
        List<Place> places = Arrays.asList(new Place("1", "Meeting Room", "conference room"));
        when(placeService.listPlaces()).thenReturn(places);

        userHandler.viewPlaces();

        verify(placeService, times(1)).listPlaces();
    }

    @Test
    public void testBookPlaceSuccess() {
        Place place = new Place("1", "Meeting Room", "conference room");
        when(scanner.nextLine()).thenReturn("1", "2024-06-25T10:00", "2024-06-25T12:00");
        when(placeService.hasPlace("1")).thenReturn(true);
        when(placeService.getPlaceById("1")).thenReturn(place);

        userHandler.bookPlace(user);

        ArgumentCaptor<LocalDateTime> startCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> endCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(bookingService).bookPlace(eq(user), eq(place), startCaptor.capture(), endCaptor.capture());

        assertEquals(LocalDateTime.of(2024, 6, 25, 10, 0), startCaptor.getValue());
        assertEquals(LocalDateTime.of(2024, 6, 25, 12, 0), endCaptor.getValue());
    }

    @Test
    public void testBookPlaceNotFound() {
        when(scanner.nextLine()).thenReturn("1");
        when(placeService.hasPlace("1")).thenReturn(false);

        userHandler.bookPlace(user);

        verify(placeService, times(1)).hasPlace("1");
        verify(bookingService, never()).bookPlace(any(), any(), any(), any());
    }

    @Test
    public void testCancelBookingSuccess() {
        when(scanner.nextLine()).thenReturn("1");
        when(bookingService.hasBooking("1")).thenReturn(true);

        userHandler.cancelBooking();

        verify(bookingService, times(1)).cancelBooking("1");
    }

    @Test
    public void testCancelBookingNotFound() {
        when(scanner.nextLine()).thenReturn("1");
        when(bookingService.hasBooking("1")).thenReturn(false);

        userHandler.cancelBooking();

        verify(bookingService, times(1)).hasBooking("1");
        verify(bookingService, never()).cancelBooking("1");
    }

    @Test
    public void testViewBookings() {
        List<Booking> bookings = Arrays.asList(new Booking("1", user, new Place("1", "Meeting Room", "conference room"), LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
        when(bookingService.listBookings()).thenReturn(bookings);

        userHandler.viewBookings();

        verify(bookingService, times(1)).listBookings();
    }

    @Test
    public void testViewBookingsByUserSuccess() {
        List<Booking> bookings = Arrays.asList(new Booking("1", user, new Place("1", "Meeting Room", "conference room"), LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
        when(scanner.nextLine()).thenReturn("testUser");
        when(userService.hasUser("testUser")).thenReturn(true);
        when(bookingService.listBookingsByUser("testUser")).thenReturn(bookings);

        userHandler.viewBookingsByUser();

        verify(bookingService, times(1)).listBookingsByUser("testUser");
    }

    @Test
    public void testViewBookingsByUserNotFound() {
        when(scanner.nextLine()).thenReturn("testUser");
        when(userService.hasUser("testUser")).thenReturn(false);

        userHandler.viewBookingsByUser();

        verify(userService, times(1)).hasUser("testUser");
        verify(bookingService, never()).listBookingsByUser("testUser");
    }

    @Test
    public void testViewBookingsByPlaceSuccess() {
        List<Booking> bookings = Arrays.asList(new Booking("1", user, new Place("1", "Meeting Room", "conference room"), LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
        when(scanner.nextLine()).thenReturn("1");
        when(placeService.hasPlace("1")).thenReturn(true);
        when(bookingService.listBookingsByPlace("1")).thenReturn(bookings);

        userHandler.viewBookingsByPlace();

        verify(bookingService, times(1)).listBookingsByPlace("1");
    }

    @Test
    public void testViewBookingsByPlaceNotFound() {
        when(scanner.nextLine()).thenReturn("1");
        when(placeService.hasPlace("1")).thenReturn(false);

        userHandler.viewBookingsByPlace();

        verify(placeService, times(1)).hasPlace("1");
        verify(bookingService, never()).listBookingsByPlace("1");
    }

    @Test
    public void testViewBookingsByDate() {
        List<Booking> bookings = Arrays.asList(new Booking("1", user, new Place("1", "Meeting Room", "conference room"), LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
        when(scanner.nextLine()).thenReturn("2024-06-25");
        when(bookingService.listBookingsByDate(LocalDate.of(2024, 6, 25))).thenReturn(bookings);

        userHandler.viewBookingsByDate();

        verify(bookingService, times(1)).listBookingsByDate(LocalDate.of(2024, 6, 25));
    }

    @Test
    public void testHandleUserActionsLogout() {
        when(scanner.nextLine()).thenReturn("8");

        userHandler.handleUserActions(user);

        verify(scanner, times(1)).nextLine();
    }

    @Test
    public void testHandleUserActionsInvalidOption() {
        when(scanner.nextLine()).thenReturn("invalid", "8");

        userHandler.handleUserActions(user);

        verify(scanner, times(2)).nextLine();
    }
}
