package de.ffmjava.capstone.backend.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private final UserRepository mockUserRepository = mock(UserRepository.class);

    private final BCryptPasswordEncoder mockPasswordEncorder = mock(BCryptPasswordEncoder.class);
    private final UserService userService = new UserService(mockUserRepository);


    @Test
    void saveSuccessful() {
        AppUser newAppUser = new AppUser("null", "username", "password", "", "", "email");

        when(mockUserRepository.save(newAppUser)).thenReturn(newAppUser);
        when(mockPasswordEncorder.encode("password")).thenReturn("encodedPassword");
        String actual = userService.save(newAppUser, mockPasswordEncorder);
        String expected = "Created user: " + newAppUser.username();

        assertEquals(expected, actual);
        verify(mockPasswordEncorder).encode("password");
    }


    @Test
    void getUserDetails() {
    }
}