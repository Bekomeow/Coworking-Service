package org.beko.controller;

import org.beko.model.Booking;
import org.beko.model.Place;
import org.beko.model.User;
import org.beko.service.AdminService;
import org.beko.service.BookingService;
import org.beko.service.PlaceService;
import org.beko.service.UserService;
import org.beko.util.ConnectionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controller class that provides methods to interact with the services
 * for admin, user, place, and booking operations.
 */

public class ServiceController {
    private final AdminService adminService;
    private final UserService userService;
    private final PlaceService placeService;
    private final BookingService bookingService;

    public ServiceController(ConnectionManager connectionManager) {
        adminService = new AdminService(connectionManager);
        userService = new UserService(connectionManager);
        placeService = new PlaceService(connectionManager);
        bookingService = new BookingService(connectionManager);
    }

    /**
     * Logs in as an admin.
     *
     * @param adminName the admin's username
     * @param password the admin's password
     * @return true if login is successful, false otherwise
     */
    public boolean loginAsAdmin(String adminName, String password) {
        return adminService.login(adminName, password);
    }


    /**
     * Registers a new user.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     * @return the registered User object
     */
    public User register(String username, String password) {
        return userService.register(username, password);
    }

    /**
     * Logs in as a user.
     *
     * @param username the user's username
     * @param password the user's password
     * @return the logged-in User object
     */
    public User loginAsUser(String username, String password) {
        return userService.login(username, password);
    }

    /**
     * Checks if a user exists.
     *
     * @param username the username to check
     * @return true if the user exists, false otherwise
     */
    public boolean hasUser(String username) {
        return userService.hasUser(username);
    }

    /**
     * Adds a new place.
     *
     * @param name the name of the place
     * @param type the type of the place
     * @return the added Place object
     */
    public Place addPlace(String name, String type) {
        return placeService.addPlace(name, type);
    }

    /**
     * Updates an existing place.
     *
     * @param id the ID of the place
     * @param name the new name of the place
     * @param type the new type of the place
     */
    public void updatePlace(Long id, String name, String type) {
        placeService.updatePlace(id, name, type);
    }

    /**
     * Checks if a place exists.
     *
     * @param id the ID of the place to check
     * @return true if the place exists, false otherwise
     */
    public boolean hasPlace(Long id) {
        return placeService.hasPlace(id);
    }

    /**
     * Deletes a place.
     *
     * @param id the ID of the place to delete
     */
    public void deletePlace(Long id) {
        placeService.deletePlace(id);
    }

    /**
     * Lists all places.
     *
     * @return a list of all Place objects
     */
    public List<Place> listPlaces() {
        return placeService.listPlaces();
    }

    /**
     * Gets a place by its ID.
     *
     * @param id the ID of the place to get
     * @return an Optional containing the Place object if found, or empty if not found
     */
    public Optional<Place> getPlaceById(Long id) {
        return placeService.getPlaceById(id);
    }

    /**
     * Books a place.
     *
     * @param user the user booking the place
     * @param place the place to be booked
     * @param startTime the start time of the booking
     * @param endTime the end time of the booking
     * @return the created Booking object
     */
    public Booking bookPlace(User user, Place place, LocalDateTime startTime, LocalDateTime endTime) {
        return bookingService.bookPlace(user, place, startTime, endTime);
    }

    /**
     * Cancels a booking.
     *
     * @param id the ID of the booking to cancel
     */
    public void cancelBooking(Long id) {
        bookingService.cancelBooking(id);
    }

    /**
     * Lists all bookings.
     *
     * @return a list of all Booking objects
     */
    public List<Booking> listBookings() {
        return bookingService.listBookings();
    }

    /**
     * Lists bookings by user.
     *
     * @param username the username of the user
     * @return a list of Booking objects for the specified user
     */
    public List<Booking> listBookingsByUser(String username) {
        return bookingService.listBookingsByUser(username);
    }

    /**
     * Lists bookings by place.
     *
     * @param placeId the ID of the place
     * @return a list of Booking objects for the specified place
     */
    public List<Booking> listBookingsByPlace(Long placeId) {
        return bookingService.listBookingsByPlace(placeId);
    }

    /**
     * Lists bookings by date.
     *
     * @param date the date of the bookings
     * @return a list of Booking objects for the specified date
     */
    public List<Booking> listBookingsByDate(LocalDate date) {
        return bookingService.listBookingsByDate(date);
    }

    /**
     * Checks if a booking exists.
     *
     * @param id the ID of the booking to check
     * @return true if the booking exists, false otherwise
     */
    public boolean hasBooking(Long id) {
        return bookingService.hasBooking(id);
    }
}
