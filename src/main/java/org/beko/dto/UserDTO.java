package org.beko.dto;

import lombok.Getter;
import lombok.Setter;
import org.beko.model.types.Role;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    Role role;
}
