package de.ffmjava.capstone.backend.clients;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
class ClientIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "Basic")
    void getAllClients_AndExpectEmptyList_200() throws Exception {
        mockMvc.perform(get
                        ("/clients/"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = "Basic")
    void addNewClient_AndExpectClient_201() throws Exception {
        String jsonString =
                """
                            {
                              "name": "Einstaller",
                              "ownsHorse": [],
                              "clientSince": "2019-01-21T05:47:08.644"
                            }
                        """;
        mockMvc.perform(post("/clients/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andExpect(jsonPath("$.ownsHorse").isEmpty())
                .andExpect(jsonPath("$.clientSince").isNotEmpty());
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = "Basic")
    void addNewClient_AndExpectErrorMessage_400() throws Exception {
        String jsonString =
                """
                            {
                              "name": "",
                              "ownsHorse": [],
                              "clientSince": "2019-01-21T05:47:08.644"
                            }
                        """;
        mockMvc.perform(post("/clients/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andExpect(status().is(400))
                .andExpect(content().string("Feld \"Name\" darf nicht leer sein"));
    }
}
