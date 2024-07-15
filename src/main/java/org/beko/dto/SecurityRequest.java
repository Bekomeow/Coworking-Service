package org.beko.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record SecurityRequest(
        @NotNull(message = "Username cannot be null")
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 3, max = 50, message = "Username length must be between 3 and 50 characters")
        String username,

        @NotNull(message = "Password cannot be null")
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 5, max = 30, message = "Password length must be between 5 and 30 characters")
        String password) {
}