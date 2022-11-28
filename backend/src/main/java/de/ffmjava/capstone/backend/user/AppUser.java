package de.ffmjava.capstone.backend.user;

import com.mongodb.lang.Nullable;
import lombok.With;

@With
public record AppUser(
        String id,
        String username,
        String rawPassword,
        String passwordBcrypt,
        String role,
        @Nullable
        String eMail
) {
}
