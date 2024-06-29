package org.beko.DAO;

import org.beko.model.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingDAO extends DAO<Long, Booking> {
    List<Booking> findByUsername(String username);

    List<Booking> findByPlaceId(Long id);

    List<Booking> findByDate(LocalDate date);
}
