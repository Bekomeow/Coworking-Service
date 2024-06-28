package org.beko.handler;

import org.beko.model.Booking;
import org.beko.model.Place;
import org.beko.model.User;
import org.beko.service.BookingService;
import org.beko.service.PlaceService;
import org.beko.service.UserService;
import org.beko.util.ScannerWrapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class UserHandler {
    private final ScannerWrapper scanner;
    private final UserService userService;
    private final PlaceService placeService;
    private final BookingService bookingService;

    public UserHandler(ScannerWrapper scanner, UserService userService, PlaceService placeService, BookingService bookingService) {
        this.scanner = scanner;
        this.userService = userService;
        this.placeService = placeService;
        this.bookingService = bookingService;
    }

    public void handleUserActions(User user) {
        while (true) {
            displayUserMenu();
            String option = scanner.nextLine();

            switch (option) {
                case "1" -> viewPlaces();
                case "2" -> bookPlace(user);
                case "3" -> cancelBooking();
                case "4" -> viewBookings();
                case "5" -> viewBookingsByUser();
                case "6" -> viewBookingsByPlace();
                case "7" -> viewBookingsByDate();
                case "8" -> {
                    System.out.println("Logout");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void displayUserMenu() {
        System.out.println("-------USER MODE-------");
        System.out.println("1. View Places");
        System.out.println("2. Book Place");
        System.out.println("3. Cancel Booking");
        System.out.println("4. View Bookings");
        System.out.println("5. View Bookings By User");
        System.out.println("6. View Bookings By Place");
        System.out.println("7. View Bookings By Date");
        System.out.println("8. Logout");
        System.out.println("-----------------------");
        System.out.print("Choose an option: ");
    }

    public void viewPlaces() {
        System.out.println("View Places");
        List<Place> places = placeService.listPlaces();
        places.forEach(System.out::println);
    }

    public void bookPlace(User user) {
        System.out.println("Book Place");
        System.out.print("Enter place ID: ");
        String placeId = scanner.nextLine();
        if (!placeService.hasPlace(placeId)) {
            System.out.println("Place not found.");
            return;
        }
        Place place = placeService.getPlaceById(placeId);
        System.out.print("Enter start time (YYYY-MM-DDTHH:MM): ");
        LocalDateTime startTime = LocalDateTime.parse(scanner.nextLine());
        System.out.print("Enter end time (YYYY-MM-DDTHH:MM): ");
        LocalDateTime endTime = LocalDateTime.parse(scanner.nextLine());
        bookingService.bookPlace(user, place, startTime, endTime);
        System.out.println("Place booked successfully.");
    }

    public void cancelBooking() {
        System.out.println("Cancel Booking");
        System.out.print("Enter booking ID: ");
        String id = scanner.nextLine();
        if (!bookingService.hasBooking(id)) {
            System.out.println("Booking not found.");
            return;
        }
        bookingService.cancelBooking(id);
        System.out.println("Booking cancelled successfully.");
    }

    public void viewBookings() {
        System.out.println("View Bookings");
        List<Booking> bookings = bookingService.listBookings();
        bookings.forEach(System.out::println);
    }

    public void viewBookingsByUser() {
        System.out.println("View Bookings By User");
        System.out.print("Enter user name: ");
        String username = scanner.nextLine();
        if (!userService.hasUser(username)) {
            System.out.println("User not found.");
            return;
        }
        List<Booking> bookingsByUser = bookingService.listBookingsByUser(username);
        bookingsByUser.forEach(System.out::println);
    }

    public void viewBookingsByPlace() {
        System.out.println("View Bookings By Place");
        System.out.print("Enter place ID: ");
        String placeId = scanner.nextLine();
        if (!placeService.hasPlace(placeId)) {
            System.out.println("Place not found.");
            return;
        }
        List<Booking> bookingsByPlace = bookingService.listBookingsByPlace(placeId);
        bookingsByPlace.forEach(System.out::println);
    }

    public void viewBookingsByDate() {
        System.out.println("View Bookings By Date");
        System.out.print("Enter date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        List<Booking> bookingsByDate = bookingService.listBookingsByDate(date);
        bookingsByDate.forEach(System.out::println);
    }
}
