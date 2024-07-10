package org.beko.service;

import org.beko.model.Booking;
import org.beko.model.Place;
import org.beko.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    /**
     * Books a place for a user for the specified time period.
     *
     * @param user      the user booking the place
     * @param place     the place to be booked
     * @param startTime the start time of the booking
     * @param endTime   the end time of the booking
     * @return the created Booking object
     * @throws IllegalArgumentException if the start time is after the end time or if the place is already booked
     */
    Booking bookPlace(User user, Place place, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Cancels a booking by its ID.
     *
     * @param id the booking ID
     */
    void cancelBooking(Long id);

    /**
     * Lists all bookings.
     *
     * @return a list of all bookings
     */
    List<Booking> listBookings();

    /**
     * Lists bookings by username.
     *
     * @param username the username
     * @return a list of bookings by the specified user
     */
    List<Booking> listBookingsByUser(String username);

    /**
     * Lists bookings by place ID.
     *
     * @param placeName the place name
     * @return a list of bookings for the specified place
     */
    List<Booking> listBookingsByPlace(String placeName);

    /**
     * Lists bookings by date.
     *
     * @param date the date
     * @return a list of bookings for the specified date
     */
    List<Booking> listBookingsByDate(LocalDate date);

    /**
     * Checks if a booking exists by its ID.
     *
     * @param id the booking ID
     * @return true if the booking exists, false otherwise
     */
    boolean hasBooking(Long id);

    List<Place> getAvailablePlacesForDate(LocalDate date);

    List<Place> getAvailablePlacesAtNow();
}
