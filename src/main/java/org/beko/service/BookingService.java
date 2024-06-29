package org.beko.service;

import org.beko.DAO.impl.BookingDAOImpl;
import org.beko.model.Booking;
import org.beko.model.Place;
import org.beko.model.User;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

public class BookingService {
    private static final BookingDAOImpl BOOKING_DAO = new BookingDAOImpl();

    public Booking bookPlace(User user, Place place, LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before  end time.");
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

    public void cancelBooking(Long id) {
        BOOKING_DAO.deleteById(id);
    }

    public List<Booking> listBookings() {
        return BOOKING_DAO.findAll();
    }

    public List<Booking> listBookingsByUser(String username) {
        return BOOKING_DAO.findByUsername(username);
    }

    public List<Booking> listBookingsByPlace(Long placeId) {
        return BOOKING_DAO.findByPlaceId(placeId);
    }

    public List<Booking> listBookingsByDate(LocalDate date) {
        return BOOKING_DAO.findByDate(date);
    }

    public boolean hasBooking(Long id) {
        Optional<Booking> maybeBooking = Optional.ofNullable(BOOKING_DAO.findById(id));
        return maybeBooking.isPresent();
    }
}
