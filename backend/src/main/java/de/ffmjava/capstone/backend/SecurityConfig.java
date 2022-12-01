package de.ffmjava.capstone.backend;

import de.ffmjava.capstone.backend.user.UserService;
import de.ffmjava.capstone.backend.user.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String PROTECTED_STOCK_PATH = "/stock/**";
    private static final String PROTECTED_HORSE_PATH = "/horses/**";
    private static final String ROLE_BASIC = "Basic";

    private final UserService userService;

    public static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Bean
    public PasswordEncoder encoder() {
        return passwordEncoder;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .httpBasic().and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/app-users").permitAll()
                .antMatchers(HttpMethod.GET, "/api/app-users/me").permitAll()
                .antMatchers(HttpMethod.GET,
                        PROTECTED_STOCK_PATH, PROTECTED_HORSE_PATH, "/api/app-users/logout", "/api/app-users/login"
                ).hasAnyRole(ROLE_BASIC)
                .antMatchers(HttpMethod.POST,
                        PROTECTED_STOCK_PATH, PROTECTED_HORSE_PATH)
                .hasAnyRole(ROLE_BASIC)
                .antMatchers(HttpMethod.PUT,
                        PROTECTED_STOCK_PATH)
                .hasAnyRole(ROLE_BASIC)
                .antMatchers(HttpMethod.DELETE,
                        PROTECTED_STOCK_PATH, PROTECTED_HORSE_PATH)
                .hasAnyRole(ROLE_BASIC)
                .anyRequest().denyAll()
                .and().formLogin()
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

