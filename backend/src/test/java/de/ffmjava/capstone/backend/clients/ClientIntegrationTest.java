package de.ffmjava.capstone.backend.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ffmjava.capstone.backend.clients.model.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
class ClientIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

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
                              "ownsHorse": []
                            }
                        """;
        mockMvc.perform(post("/clients/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andExpect(jsonPath("$.ownsHorse").isEmpty());
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = "Basic")
    void addNewClient_AndExpectErrorMessage_400() throws Exception {
        String jsonString =
                """
                            {
                              "name": "",
                              "ownsHorse": []
                            }
                        """;
        mockMvc.perform(post("/clients/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andExpect(status().is(400))
                .andExpect(content().string("Feld \"Name\" darf nicht leer sein"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = "Basic")
    void deleteClient_AndExpect_204() throws Exception {
        String jsonString =
                """
                            {
                              "name": "Name",
                              "ownsHorse": []
                            }
                        """;
        String postResponse = mockMvc.perform(post("/clients/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
        ).andReturn().getResponse().getContentAsString();


        String idToDelete = objectMapper.readValue(postResponse, Client.class).id();

        mockMvc.perform(delete
                        ("/clients/" + idToDelete))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "Basic")
    void deleteClient_AndExpect_404() throws Exception {
        mockMvc.perform(delete
                        ("/clients/1"))
                .andExpect(status().isNotFound());
    }
}
