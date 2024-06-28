package org.beko;

import org.beko.model.Booking;
import org.beko.model.Place;
import org.beko.model.User;
import org.beko.service.BookingService;
import org.beko.service.PlaceService;
import org.beko.service.UserService;
import org.beko.util.ScannerWrapper;
import org.beko.handler.UserHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class UserHandlerTest {

    private ScannerWrapper scanner;
    private UserService userService;
    private PlaceService placeService;
    private BookingService bookingService;
    private UserHandler userHandler;
    private User user;

    @BeforeEach
    public void setUp() {
        scanner = Mockito.mock(ScannerWrapper.class);
        userService = Mockito.mock(UserService.class);
        placeService = Mockito.mock(PlaceService.class);
        bookingService = Mockito.mock(BookingService.class);
        userHandler = new UserHandler(scanner, userService, placeService, bookingService);
        user = Mockito.mock(User.class);
    }

    @Test
    public void testViewPlaces() {
        List<Place> places = Arrays.asList(new Place("1", "Meeting Room", "conference room"));
        Mockito.when(placeService.listPlaces()).thenReturn(places);

        userHandler.viewPlaces();

        Mockito.verify(placeService, Mockito.times(1)).listPlaces();
    }

    @Test
    public void testBookPlaceSuccess() {
        Place place = new Place("1", "Meeting Room", "conference room");
        Mockito.when(scanner.nextLine()).thenReturn("1", "2024-06-25T10:00", "2024-06-25T12:00");
        Mockito.when(placeService.hasPlace("1")).thenReturn(true);
        Mockito.when(placeService.getPlaceById("1")).thenReturn(place);

        userHandler.bookPlace(user);

        ArgumentCaptor<LocalDateTime> startCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> endCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        Mockito.verify(bookingService).bookPlace(ArgumentMatchers.eq(user), ArgumentMatchers.eq(place), startCaptor.capture(), endCaptor.capture());

        Assertions.assertEquals(LocalDateTime.of(2024, 6, 25, 10, 0), startCaptor.getValue());
        Assertions.assertEquals(LocalDateTime.of(2024, 6, 25, 12, 0), endCaptor.getValue());
    }

    @Test
    public void testBookPlaceNotFound() {
        Mockito.when(scanner.nextLine()).thenReturn("1");
        Mockito.when(placeService.hasPlace("1")).thenReturn(false);

        userHandler.bookPlace(user);

        Mockito.verify(placeService, Mockito.times(1)).hasPlace("1");
        Mockito.verify(bookingService, Mockito.never()).bookPlace(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testCancelBookingSuccess() {
        Mockito.when(scanner.nextLine()).thenReturn("1");
        Mockito.when(bookingService.hasBooking("1")).thenReturn(true);

        userHandler.cancelBooking();

        Mockito.verify(bookingService, Mockito.times(1)).cancelBooking("1");
    }

    @Test
    public void testCancelBookingNotFound() {
        Mockito.when(scanner.nextLine()).thenReturn("1");
        Mockito.when(bookingService.hasBooking("1")).thenReturn(false);

        userHandler.cancelBooking();

        Mockito.verify(bookingService, Mockito.times(1)).hasBooking("1");
        Mockito.verify(bookingService, Mockito.never()).cancelBooking("1");
    }

    @Test
    public void testViewBookings() {
        List<Booking> bookings = Arrays.asList(new Booking("1", user, new Place("1", "Meeting Room", "conference room"), LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
        Mockito.when(bookingService.listBookings()).thenReturn(bookings);

        userHandler.viewBookings();

        Mockito.verify(bookingService, Mockito.times(1)).listBookings();
    }

    @Test
    public void testViewBookingsByUserSuccess() {
        List<Booking> bookings = Arrays.asList(new Booking("1", user, new Place("1", "Meeting Room", "conference room"), LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
        Mockito.when(scanner.nextLine()).thenReturn("testUser");
        Mockito.when(userService.hasUser("testUser")).thenReturn(true);
        Mockito.when(bookingService.listBookingsByUser("testUser")).thenReturn(bookings);

        userHandler.viewBookingsByUser();

        Mockito.verify(bookingService, Mockito.times(1)).listBookingsByUser("testUser");
    }

    @Test
    public void testViewBookingsByUserNotFound() {
        Mockito.when(scanner.nextLine()).thenReturn("testUser");
        Mockito.when(userService.hasUser("testUser")).thenReturn(false);

        userHandler.viewBookingsByUser();

        Mockito.verify(userService, Mockito.times(1)).hasUser("testUser");
        Mockito.verify(bookingService, Mockito.never()).listBookingsByUser("testUser");
    }

    @Test
    public void testViewBookingsByPlaceSuccess() {
        List<Booking> bookings = Arrays.asList(new Booking("1", user, new Place("1", "Meeting Room", "conference room"), LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
        Mockito.when(scanner.nextLine()).thenReturn("1");
        Mockito.when(placeService.hasPlace("1")).thenReturn(true);
        Mockito.when(bookingService.listBookingsByPlace("1")).thenReturn(bookings);

        userHandler.viewBookingsByPlace();

        Mockito.verify(bookingService, Mockito.times(1)).listBookingsByPlace("1");
    }

    @Test
    public void testViewBookingsByPlaceNotFound() {
        Mockito.when(scanner.nextLine()).thenReturn("1");
        Mockito.when(placeService.hasPlace("1")).thenReturn(false);

        userHandler.viewBookingsByPlace();

        Mockito.verify(placeService, Mockito.times(1)).hasPlace("1");
        Mockito.verify(bookingService, Mockito.never()).listBookingsByPlace("1");
    }

    @Test
    public void testViewBookingsByDate() {
        List<Booking> bookings = Arrays.asList(new Booking("1", user, new Place("1", "Meeting Room", "conference room"), LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
        Mockito.when(scanner.nextLine()).thenReturn("2024-06-25");
        Mockito.when(bookingService.listBookingsByDate(LocalDate.of(2024, 6, 25))).thenReturn(bookings);

        userHandler.viewBookingsByDate();

        Mockito.verify(bookingService, Mockito.times(1)).listBookingsByDate(LocalDate.of(2024, 6, 25));
    }

    @Test
    public void testHandleUserActionsLogout() {
        Mockito.when(scanner.nextLine()).thenReturn("8");

        userHandler.handleUserActions(user);

        Mockito.verify(scanner, Mockito.times(1)).nextLine();
    }

    @Test
    public void testHandleUserActionsInvalidOption() {
        Mockito.when(scanner.nextLine()).thenReturn("invalid", "8");

        userHandler.handleUserActions(user);

        Mockito.verify(scanner, Mockito.times(2)).nextLine();
    }
}
