package org.beko.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    Long id;
    String username;
    String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
