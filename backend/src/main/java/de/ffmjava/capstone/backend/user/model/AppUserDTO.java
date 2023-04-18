package de.ffmjava.capstone.backend.user.model;

import com.mongodb.lang.Nullable;
import lombok.With;

import javax.validation.constraints.NotBlank;

@With
public record AppUserDTO(
        String id,
        @NotBlank(message = "Username darf nicht leer sein")
        String username,
        String role,
        @Nullable
        String eMail
) {
    public static AppUserDTO createDTOFromUser(AppUser appUser) {
        return new AppUserDTO(appUser.id(),
                appUser.username(),
                appUser.role(),
                appUser.eMail());
    }
}
