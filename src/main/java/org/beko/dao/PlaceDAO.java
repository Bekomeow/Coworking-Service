package org.beko.dao;

import org.beko.model.Booking;
import org.beko.model.Place;


/**
 * DAO interface for Place entity operations.
 */
public interface PlaceDAO extends DAO<Long, Place> {
    Place findByPlaceName(String placeName);
}
