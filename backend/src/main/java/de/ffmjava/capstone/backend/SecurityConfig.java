package de.ffmjava.capstone.backend;

import de.ffmjava.capstone.backend.user.UserService;
import de.ffmjava.capstone.backend.user.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private static final String PROTECTED_STOCK_PATH = "/stock/**";
    private static final String PROTECTED_HORSE_PATH = "/horses/**";
    private static final String PROTECTED_CLIENT_PATH = "/clients/**";

    private static final String ROLE_BASIC = "Basic";

    private final UserService userService;

    public static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Bean
    public PasswordEncoder encoder() {
        return passwordEncoder;
    }

    private static class NoPopupBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
                             AuthenticationException authException) throws IOException {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        }
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .httpBasic().authenticationEntryPoint(new NoPopupBasicAuthenticationEntryPoint()).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/app-users").permitAll()
                .antMatchers(HttpMethod.GET, "/api/app-users/me").permitAll()
                .antMatchers(HttpMethod.GET,
                        PROTECTED_STOCK_PATH, PROTECTED_HORSE_PATH, PROTECTED_CLIENT_PATH,
                        "/api/app-users/logout", "/api/app-users/login", "/api/app-users/account-details/"
                ).hasAnyRole(ROLE_BASIC)
                .antMatchers(HttpMethod.POST,
                        PROTECTED_STOCK_PATH, PROTECTED_HORSE_PATH, PROTECTED_CLIENT_PATH)
                .hasAnyRole(ROLE_BASIC)
                .antMatchers(HttpMethod.PUT,
                        PROTECTED_STOCK_PATH, PROTECTED_HORSE_PATH, PROTECTED_CLIENT_PATH)
                .hasAnyRole(ROLE_BASIC)
                .antMatchers(HttpMethod.DELETE,
                        PROTECTED_STOCK_PATH, PROTECTED_HORSE_PATH, PROTECTED_CLIENT_PATH)
                .hasAnyRole(ROLE_BASIC)
                .and().formLogin().loginPage("/")
                .and().build();
    }

    @Bean
    public UserDetailsManager userDetailsService() {
        return new UserDetailsManager() {
            static final String EXCEPTIONMESSAGE = "Method invocation not possible";

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                AppUser userByName = userService.findByUsername(username);
                if (userByName == null) {
                    throw new UsernameNotFoundException("Username not found");
                }
                return User.builder()
                        .username(username)
                        .password(userByName.passwordBcrypt())
                        .roles(userByName.role())
                        .build();
            }

            @Override
            public void createUser(UserDetails user) {
                throw new UnsupportedOperationException(EXCEPTIONMESSAGE);
            }

            @Override
            public void updateUser(UserDetails user) {
                throw new UnsupportedOperationException(EXCEPTIONMESSAGE);
            }

            @Override
            public void deleteUser(String username) {
                throw new UnsupportedOperationException(EXCEPTIONMESSAGE);
            }

            @Override
            public void changePassword(String oldPassword, String newPassword) {
                throw new UnsupportedOperationException(EXCEPTIONMESSAGE);
            }

            @Override
            public boolean userExists(String username) {
                throw new UnsupportedOperationException(EXCEPTIONMESSAGE);
            }
        };

    }


}

