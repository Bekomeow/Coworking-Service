package org.beko.Service;

import org.beko.Entity.Booking;
import org.beko.Entity.Place;
import org.beko.Entity.User;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class BookingService {
    HashMap<String, Booking> bookings = new HashMap<>();
    private int bookingIdCounter = 0;

    public Booking bookPlace(User user, Place place, LocalDateTime startTime, LocalDateTime endTime) {
        if(startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before  end time.");
        }

        for(Booking booking: bookings.values()) {
            if(booking.getPlace().getId().equals(place.getId()) &&
                    booking.getStartTime().isBefore(endTime) &&
                    booking.getEndTime().isAfter(startTime)) {
                throw new IllegalArgumentException("Resource is already booked for the selected time.");
            }
        }

        String id = String.valueOf(bookingIdCounter++);
        Booking booking = new Booking(id, user, place, startTime, endTime);
        bookings.put(id, booking);
        return booking;
    }

    public void cancelBooking(String id) {
        bookings.remove(id);
    }

    public List<Booking> listBookings() {
        return new ArrayList<>(bookings.values());
    }

    public List<Booking> listBookingsByUser(String username) {
        List<Booking> userBookings = new ArrayList<>();

        for(Booking booking: bookings.values()) {
            if(booking.getUser().getUsername().equals(username)) {
                userBookings.add(booking);
            }
        }
        return userBookings;
    }

    public List<Booking> listBookingsByPlace(String placeId) {
        List<Booking> placeBookings = new ArrayList<>();

        for(Booking booking: bookings.values()) {
            if(booking.getPlace().getId().equals(placeId)) {
                placeBookings.add(booking);
            }
        }
        return placeBookings;
    }

    public List<Booking> listBookingsByDate(LocalDate date) {
        List<Booking> dateBookings = new ArrayList<>();

        for(Booking booking: bookings.values()) {
            if(booking.getStartTime().toLocalDate().equals(date)) {
                dateBookings.add(booking);
            }
        }
        return dateBookings;
    }

    public boolean hasBooking(String id) {
        return bookings.containsKey(id);
    }
}
