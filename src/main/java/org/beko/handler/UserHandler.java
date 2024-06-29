package org.beko.handler;

import lombok.RequiredArgsConstructor;
import org.beko.controller.ServiceController;
import org.beko.model.Booking;
import org.beko.model.Place;
import org.beko.model.User;
import org.beko.wrapper.ScannerWrapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Handles user actions such as viewing places, booking places, and viewing bookings.
 */
@RequiredArgsConstructor
public class UserHandler {
    private final ScannerWrapper scanner;
    private final ServiceController serviceController;

    /**
     * Handles the user actions by displaying the menu and processing user input.
     *
     * @param user the user performing the actions
     */
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

    /**
     * Displays the user menu.
     */
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

    /**
     * Views all places.
     */
    public void viewPlaces() {
        System.out.println("View Places");
        List<Place> places = serviceController.listPlaces();
        places.forEach(System.out::println);
    }

    /**
     * Books a place for the user.
     *
     * @param user the user booking the place
     */
    public void bookPlace(User user) {
        System.out.println("Book Place");
        System.out.print("Enter place ID: ");
        Long placeId = Long.valueOf(scanner.nextLine());
        if (!serviceController.hasPlace(placeId)) {
            System.out.println("Place not found.");
            return;
        }
        Place place = serviceController.getPlaceById(placeId).get();
        System.out.print("Enter start time (YYYY-MM-DDTHH:MM): ");
        LocalDateTime startTime = LocalDateTime.parse(scanner.nextLine());
        System.out.print("Enter end time (YYYY-MM-DDTHH:MM): ");
        LocalDateTime endTime = LocalDateTime.parse(scanner.nextLine());
        serviceController.bookPlace(user, place, startTime, endTime);
        System.out.println("Place booked successfully.");
    }

    /**
     * Cancels a booking.
     */
    public void cancelBooking() {
        System.out.println("Cancel Booking");
        System.out.print("Enter booking ID: ");
        Long id = Long.valueOf(scanner.nextLine());
        if (!serviceController.hasBooking(id)) {
            System.out.println("Booking not found.");
            return;
        }
        serviceController.cancelBooking(id);
        System.out.println("Booking cancelled successfully.");
    }

    /**
     * Views all bookings.
     */
    public void viewBookings() {
        System.out.println("View Bookings");
        List<Booking> bookings = serviceController.listBookings();
        bookings.forEach(System.out::println);
    }

    /**
     * Views bookings by a specific user.
     */
    public void viewBookingsByUser() {
        System.out.println("View Bookings By User");
        System.out.print("Enter user name: ");
        String username = scanner.nextLine();
        if (!serviceController.hasUser(username)) {
            System.out.println("User not found.");
            return;
        }
        List<Booking> bookingsByUser = serviceController.listBookingsByUser(username);
        bookingsByUser.forEach(System.out::println);
    }

    /**
     * Views bookings by a specific place.
     */
    public void viewBookingsByPlace() {
        System.out.println("View Bookings By Place");
        System.out.print("Enter place ID: ");
        Long placeId = Long.valueOf(scanner.nextLine());
        if (!serviceController.hasPlace(placeId)) {
            System.out.println("Place not found.");
            return;
        }
        List<Booking> bookingsByPlace = serviceController.listBookingsByPlace(placeId);
        bookingsByPlace.forEach(System.out::println);
    }

    /**
     * Views bookings by a specific date.
     */
    public void viewBookingsByDate() {
        System.out.println("View Bookings By Date");
        System.out.print("Enter date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        List<Booking> bookingsByDate = serviceController.listBookingsByDate(date);
        bookingsByDate.forEach(System.out::println);
    }
}
