package org.beko.dto;

import lombok.Data;
import org.beko.model.types.Role;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    Role role;
}
