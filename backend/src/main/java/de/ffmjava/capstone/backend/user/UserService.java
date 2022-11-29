package de.ffmjava.capstone.backend.user;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public AppUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public String save(AppUser newAppUser, PasswordEncoder passwordEncoder) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(newAppUser.username()) != null) {
            throw new UserAlreadyExistsException("""
                    {"userAlreadyExists": "User with that username already exists"}
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

    public String getUserDetails(String username) {
        if (!username.equals("anonymousUser") && userRepository.existsByUsername(username)) {
            String eMail;
            try {
                eMail = userRepository.findByUsername(username).eMail();
            } catch (NullPointerException e) {
                eMail = "";
            }

            assert eMail != null;
            return """
                    {"username":"<username>",
                    "eMail":"<eMail>"}"""
                    .replace("<username>", username).replace("<eMail>", eMail);
        }
        return """
                {"username":"<username>"}
                """
                .replace("<username>", username);
    }
}
