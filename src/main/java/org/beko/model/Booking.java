package org.beko.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a booking in the system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    private Long id;
    private User user;
    private Place place;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
