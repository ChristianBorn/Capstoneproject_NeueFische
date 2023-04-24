package de.ffmjava.capstone.backend.user;


import de.ffmjava.capstone.backend.user.model.AppUser;
import de.ffmjava.capstone.backend.user.model.AppUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    public static final String ACCOUNT_DETAILS = "ACCOUNT_DETAILS";
    private final UserRepository userRepository;

    public AppUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Cacheable(value = ACCOUNT_DETAILS)
    public AppUserDTO getDTOByUsername(String username) {
        return AppUserDTO.createDTOFromUser(userRepository.findByUsername(username));
    }

    public String save(AppUser newAppUser, PasswordEncoder passwordEncoder) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(newAppUser.username()) != null) {
            throw new UserAlreadyExistsException("""
                    {"userAlreadyExists": "User mit dem angegebenen Username existiert bereits"}
                    """);
        }
        AppUser appUser = newAppUser
                .withId(UUID.randomUUID().toString())
                .withRole("Basic")
                .withPasswordBcrypt(passwordEncoder.encode(newAppUser.rawPassword()))
                .withRawPassword("");
        userRepository.save(appUser);
        return "Created user: " + newAppUser.username();
    }
}
