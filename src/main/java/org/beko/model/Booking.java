package org.beko.model;

import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Represents a booking in the system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    Long id;
    User user;
    Place place;
    LocalDateTime startTime;
    LocalDateTime endTime;

    /**
     * Constructs a new Booking with the specified user, place, start time, and end time.
     *
     * @param user      the user making the booking
     * @param place     the place being booked
     * @param startTime the start time of the booking
     * @param endTime   the end time of the booking
     */
    public Booking(User user, Place place, LocalDateTime startTime, LocalDateTime endTime) {
        this.user = user;
        this.place = place;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
