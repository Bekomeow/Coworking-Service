package org.beko.model;

import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

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

    public Booking(User user, Place place, LocalDateTime startTime, LocalDateTime endTime) {
        this.user = user;
        this.place = place;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
