package de.ffmjava.capstone.backend.user;

import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static MockWebServer mockWebServer;

    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @DynamicPropertySource
    static void backendProperties(DynamicPropertyRegistry registry) {
        registry.add("myApp.baseURL", () -> mockWebServer.url("/").toString());
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void expect401_whenNoAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/stock/overview"))
                .andExpect(status().is(401));
    }

    @Test
    @WithMockUser
    void expect403_whenAuthenticatedButUnauthorised() throws Exception {
        mockMvc.perform(get("/stock/overview"))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(roles = "Basic")
    void expect200_whenAuthenticatedAndAuthorised() throws Exception {
        mockMvc.perform(get("/stock/overview"))
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(roles = "Basic")
    void expect200_andOk_whenAuthenticatedAndAuthorised() throws Exception {
        mockMvc.perform(get("/api/app-users/login"))
                .andExpect(status().is(200))
                .andExpect(content().string("ok"));
    }

    @Test
    @WithMockUser(roles = "Basic")
    void expect200_OnLogout_whenAuthenticatedAndAuthorised() throws Exception {
        mockMvc.perform(get("/api/app-users/logout"))
                .andExpect(status().is(200));
    }


    @Test
    void expect200_and_anonymousUser() throws Exception {
        mockMvc.perform(get("/api/app-users/me"))
                .andExpect(status().is(200))
                .andExpect(content().string("anonymousUser"));
    }

    @Test
    @WithMockUser(username = "Test", roles = "Basic")
    void expect200_and_UserDetails() throws Exception {
        mockMvc.perform(get("/api/app-users/me"))
                .andExpect(status().is(200))
                .andExpect(content().string("Test"));
    }

    @Test
    void register_expect201() throws Exception {
        String jsonString =
                """
                            {
                                "username": "Chris",
                                "rawPassword": "Password123!"
                            }
                        """;
        mockMvc.perform(post("/api/app-users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().is(201))
                .andExpect(content().string("User erfolgreich registriert!"));
    }

    @Test
    @DirtiesContext
    void register_expect400() throws Exception {
        String jsonString =
                """
                            {
                                "username": "Chris",
                                "rawPassword": "Password123!"
                            }
                        """;
        mockMvc.perform(post("/api/app-users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().is(409))
                .andExpect(content().json("{\"userAlreadyExists\": \"User mit dem angegebenen Username existiert bereits\"}"));
    }

    @Test
    @DirtiesContext
    void register_expectValidationError_Username() throws Exception {
        String jsonString =
                """
                            {
                                "username": "",
                                "rawPassword": "Password123!"
                            }
                        """;
        mockMvc.perform(post("/api/app-users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().is(400))
                .andExpect(content().json("{\"username\":[\"Username darf nicht leer sein\"]}"));
    }

    @Test
    @DirtiesContext
    void register_expectValidationError_Password() throws Exception {
        String jsonString =
                """
                            {
                                "username": "name",
                                "rawPassword": ""
                            }
                        """;
        mockMvc.perform(post("/api/app-users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().is(400))
                .andExpect(content().json("{\"rawPassword\":[\"Passwort darf nicht leer sein\", \"Passwort muss mindestens acht Zeichen, ein Sonderzeichen und eine Zahl enthalten\"]}"));
    }

    @Test
    @DirtiesContext
    void register_expectValidationError_BothFieldsEmpty() throws Exception {
        String jsonString =
                """
                            {
                                "username": "",
                                "rawPassword": ""
                            }
                        """;
        mockMvc.perform(post("/api/app-users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().is(400))
                .andExpect(content().string("{\"rawPassword\":[\"Passwort darf nicht leer sein\",\"Passwort muss mindestens acht Zeichen, ein Sonderzeichen und eine Zahl enthalten\"],\"username\":[\"Username darf nicht leer sein\"]}"));
    }

    @Test
    @WithMockUser(username = "Chris", roles = "Basic")
    void expect200_and_AccountDetails() throws Exception {
        String jsonString =
                """
                            {
                                "username": "Chris",
                                "rawPassword": "Password123!"
                            }
                        """;
        mockMvc.perform(post("/api/app-users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString));
        mockMvc.perform(get("/api/app-users/account-details"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.username").value("Chris"))
                .andExpect(jsonPath("$.role").value("Basic"))
                .andExpect(jsonPath("$.eMail").isEmpty());
    }
}
