package org.beko.dao;

import org.beko.model.Booking;
import org.beko.model.Place;

import java.time.LocalDate;
import java.util.List;

/**
 * DAO interface for Booking entity operations.
 */
public interface BookingDAO extends DAO<Long, Booking> {
    /**
     * Finds bookings by username.
     *
     * @param username the username of the user
     * @return a list of Booking objects for the specified username
     */
    List<Booking> findByUsername(String username);

    /**
     * Finds bookings by place ID.
     *
     * @param  placeName the name of the place
     * @return a list of Booking objects for the specified place name
     */
    List<Booking> findByPlaceName(String placeName);

    /**
     * Finds bookings by date.
     *
     * @param date the date of the bookings
     * @return a list of Booking objects for the specified date
     */
    List<Booking> findByDate(LocalDate date);

    List<Place> findAllAvailablePlacesForDate(LocalDate date);

    List<Place> findAllAvailablePlaces();
}
