package de.ffmjava.capstone.backend.user;

import de.ffmjava.capstone.backend.user.model.AppUser;
import de.ffmjava.capstone.backend.user.model.AppUserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private final UserRepository mockUserRepository = mock(UserRepository.class);
    private final BCryptPasswordEncoder mockPasswordEncoder = mock(BCryptPasswordEncoder.class);
    private final UserService userService = new UserService(mockUserRepository);


    @Test
    void saveSuccessful() {
        //Given
        AppUser newAppUser = new AppUser("null", "username", "password", "", "", "email");
        newAppUser = newAppUser.withEMail("email@email.de").withUsername("Different username");
        //When
        when(mockUserRepository.save(newAppUser)).thenReturn(newAppUser);
        when(mockPasswordEncoder.encode("password")).thenReturn("encodedPassword");
        //Then
        String actual = userService.save(newAppUser, mockPasswordEncoder);
        String expected = "Created user: " + newAppUser.username();

        assertEquals(expected, actual);
        verify(mockPasswordEncoder).encode("password");
    }

    @Test
    void findByUsername() {
        //Given
        AppUser appUser = new AppUser("null", "username", "password", "", "", "email");
        //When
        when(mockUserRepository.findByUsername("username")).thenReturn(appUser);
        //Then
        AppUser expected = appUser;
        AppUser actual = userService.findByUsername("username");

        assertEquals(expected, actual);
    }

    @Test
    void getDTOByUsername() {
        //Given
        AppUser appUser = new AppUser("null", "username", "password", "", "", "email");
        AppUserDTO appUserDTO = new AppUserDTO("null", "username", "", "email");
        //When
        when(mockUserRepository.findByUsername("username")).thenReturn(appUser);
        //Then
        AppUserDTO expected = appUserDTO;
        AppUserDTO actual = userService.getDTOByUsername("username");

        assertEquals(expected, actual);
    }

    @Test
    void createAppUserDTOFromAppUser() {
        //Given
        AppUser appUser = new AppUser("null", "username", "password", "", "", "email");
        AppUserDTO appUserDTO = new AppUserDTO("null", "username", "", "email");
        //When
        //Then
        AppUserDTO expected = appUserDTO;
        AppUserDTO actual = AppUserDTO.createDTOFromUser(appUser);

        assertEquals(expected, actual);
    }
}