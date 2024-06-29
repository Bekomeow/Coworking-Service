package org.beko.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Represents an admin in the system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class Admin {
    Long id;
    String adminName;
    String adminPassword;
}
