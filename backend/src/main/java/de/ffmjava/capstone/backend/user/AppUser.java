package de.ffmjava.capstone.backend.user;

import com.mongodb.lang.Nullable;
import lombok.With;

import javax.validation.constraints.NotBlank;

@With
public record AppUser(
        String id,
        @NotBlank(message = "Username darf nicht leer sein")
        String username,
        @NotBlank(message = "Passwort darf nicht leer sein")
        String rawPassword,
        String passwordBcrypt,
        String role,
        @Nullable
        String eMail
) {
}
