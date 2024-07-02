package org.beko.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an admin in the system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Admin {
    private Long id;
    private String adminName;
    private String adminPassword;
}
