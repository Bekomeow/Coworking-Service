package org.beko.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a place in the system, such as a workspace or conference room.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Place {
    private Long id;
    private String name;
    private String type; //workspace or conference-room

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
