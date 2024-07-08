package org.beko.model;

import lombok.*;

/**
 * Represents a place in the system, such as a workspace or conference room.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place {
    private Long id;
    private String name;
    private String type; //workspace or conference-room
}
