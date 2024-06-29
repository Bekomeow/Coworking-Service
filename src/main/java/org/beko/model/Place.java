package org.beko.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Represents a place in the system, such as a workspace or conference room.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Place {
    Long id;
    String name;
    String type; //workspace or conference-room

    /**
     * Constructs a new Place with the specified name and type.
     *
     * @param name the name of the place
     * @param type the type of the place (workspace or conference-room)
     */
    public Place(String name, String type) {
        this.name = name;
        this.type = type;
    }
}
