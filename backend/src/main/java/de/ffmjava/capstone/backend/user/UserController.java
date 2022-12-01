package de.ffmjava.capstone.backend.user;

import de.ffmjava.capstone.backend.SecurityConfig;
import de.ffmjava.capstone.backend.user.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/app-users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/login")
    public ResponseEntity<String> login() {
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @GetMapping("/logout")
    public void logout(HttpSession httpSession) {
        httpSession.invalidate();
    }

    @GetMapping("/me")
    public String me() {
       return SecurityContextHolder
               .getContext()
               .getAuthentication()
               .getName();
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<String> registration(@Valid @RequestBody AppUser newAppUser, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity<>(new CustomApiErrorHandler(errors).getJsonString(), HttpStatus.BAD_REQUEST);
        }
        try {
            service.save(newAppUser, SecurityConfig.passwordEncoder);
            return new ResponseEntity<>("User erfolgreich registriert!", HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}

