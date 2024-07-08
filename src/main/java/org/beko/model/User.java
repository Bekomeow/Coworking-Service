package org.beko.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beko.model.types.Role;

/**
 * Represents a user in the system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String username;
    private String password;

    @Builder.Default
    private Role role = Role.USER;

    /**
     * Constructs a new User with the specified username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     */
}
