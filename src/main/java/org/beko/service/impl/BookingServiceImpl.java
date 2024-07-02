package org.beko.service.impl;

import org.beko.DAO.impl.BookingDAOImpl;
import org.beko.model.Booking;
import org.beko.model.Place;
import org.beko.model.User;
import org.beko.service.BookingService;
import org.beko.util.ConnectionManager;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service class for handling booking operations.
 */
public class BookingServiceImpl implements BookingService {
    private final BookingDAOImpl BOOKING_DAO;

    public BookingServiceImpl(ConnectionManager connectionManager) {
        BOOKING_DAO = new BookingDAOImpl(connectionManager);
    }

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
    public Booking bookPlace(User user, Place place, LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }

        var bookings = BOOKING_DAO.findAll();

        for (Booking booking: bookings) {
            if (booking.getPlace().getId().equals(place.getId()) &&
                    booking.getStartTime().isBefore(endTime) &&
                    booking.getEndTime().isAfter(startTime)) {
                throw new IllegalArgumentException("Resource is already booked for the selected time.");
            }
        }

        Booking booking = new Booking(user, place, startTime, endTime);
        BOOKING_DAO.save(booking);
        return booking;
    }

    /**
     * Cancels a booking by its ID.
     *
     * @param id the booking ID
     */
    public void cancelBooking(Long id) {
        BOOKING_DAO.deleteById(id);
    }

    /**
     * Lists all bookings.
     *
     * @return a list of all bookings
     */
    public List<Booking> listBookings() {
        return BOOKING_DAO.findAll();
    }

    /**
     * Lists bookings by username.
     *
     * @param username the username
     * @return a list of bookings by the specified user
     */
    public List<Booking> listBookingsByUser(String username) {
        return BOOKING_DAO.findByUsername(username);
    }

    /**
     * Lists bookings by place ID.
     *
     * @param placeId the place ID
     * @return a list of bookings for the specified place
     */
    public List<Booking> listBookingsByPlace(Long placeId) {
        return BOOKING_DAO.findByPlaceId(placeId);
    }

    /**
     * Lists bookings by date.
     *
     * @param date the date
     * @return a list of bookings for the specified date
     */
    public List<Booking> listBookingsByDate(LocalDate date) {
        return BOOKING_DAO.findByDate(date);
    }

    /**
     * Checks if a booking exists by its ID.
     *
     * @param id the booking ID
     * @return true if the booking exists, false otherwise
     */
    public boolean hasBooking(Long id) {
        Optional<Booking> maybeBooking = Optional.ofNullable(BOOKING_DAO.findById(id));
        return maybeBooking.isPresent();
    }
}
