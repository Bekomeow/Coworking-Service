package org.beko.controller;

import lombok.RequiredArgsConstructor;
import org.beko.model.Admin;
import org.beko.model.Booking;
import org.beko.model.Place;
import org.beko.model.User;
import org.beko.service.AdminService;
import org.beko.service.BookingService;
import org.beko.service.PlaceService;
import org.beko.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ServiceController {
    private static final AdminService adminService = new AdminService();
    private static final UserService userService = new UserService();
    private static final PlaceService placeService = new PlaceService();
    private static final BookingService bookingService = new BookingService();

    public boolean loginAsAdmin(String adminName, String password) {
        return adminService.login(adminName, password);
    }


    public User register(String username, String password) {
        return userService.register(username, password);
    }

    public User loginAsUser(String username, String password) {
        return userService.login(username, password);
    }

    public boolean hasUser(String username) {
        return userService.hasUser(username);
    }


    public Place addPlace(String name, String type) {
        return placeService.addPlace(name, type);
    }

    public void updatePlace(Long id, String name, String type) {
        placeService.updatePlace(id, name, type);
    }

    public boolean hasPlace(Long id) {
        return placeService.hasPlace(id);
    }

    public void deletePlace(Long id) {
        placeService.deletePlace(id);
    }

    public List<Place> listPlaces() {
        return placeService.listPlaces();
    }

    public Optional<Place> getPlaceById(Long id) {
        return placeService.getPlaceById(id);
    }


    public Booking bookPlace(User user, Place place, LocalDateTime startTime, LocalDateTime endTime) {
        return bookingService.bookPlace(user, place, startTime, endTime);
    }

    public void cancelBooking(Long id) {
        bookingService.cancelBooking(id);
    }

    public List<Booking> listBookings() {
        return bookingService.listBookings();
    }

    public List<Booking> listBookingsByUser(String username) {
        return bookingService.listBookingsByUser(username);
    }

    public List<Booking> listBookingsByPlace(Long placeId) {
        return bookingService.listBookingsByPlace(placeId);
    }

    public List<Booking> listBookingsByDate(LocalDate date) {
        return bookingService.listBookingsByDate(date);
    }

    public boolean hasBooking(Long id) {
        return bookingService.hasBooking(id);
    }
}
