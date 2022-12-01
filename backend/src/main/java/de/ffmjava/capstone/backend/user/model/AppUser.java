package de.ffmjava.capstone.backend.user.model;

import com.mongodb.lang.Nullable;
import lombok.With;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@With
public record AppUser(
        String id,
        @NotBlank(message = "Username darf nicht leer sein")
        String username,
        @NotBlank(message = "Passwort darf nicht leer sein")
        @Pattern(regexp = "^(?=[^A-Z]*+[A-Z])(?=[^a-z]*+[a-z])(?=\\D*+\\d)(?=[^#?!@$ %^&*-]*+[#?!@$ %^&*-]).{8,}$",
                message = "Passwort muss mindestens acht Zeichen, ein Sonderzeichen und eine Zahl enthalten")
        String rawPassword,
        String passwordBcrypt,
        String role,
        @Nullable
        String eMail
) {
}
