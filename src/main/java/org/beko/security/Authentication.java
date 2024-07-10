package org.beko.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beko.model.types.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authentication {
    private String username;
    private Role role;
    private boolean isAuth;
    private String message;
}
