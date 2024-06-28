package org.beko.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Place {
    Long id;
    String name;
    String type; //workspace or conference-room

    public Place(String name, String type) {
        this.name = name;
        this.type = type;
    }
}
