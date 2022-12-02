package de.ffmjava.capstone.backend.horses;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ffmjava.capstone.backend.horses.model.Horse;
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
class HorseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "Basic")
    void getAllHorses() throws Exception {
        mockMvc.perform(get
                        ("/horses/"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = "Basic")
    void addNewHorse_AndExpectSuccessMessage_201() throws Exception {
        String jsonString =
                """
                            {
                              "name": "Hansi",
                              "owner": "Peter Pan",
                              "consumptionList": []
                            }
                        """;
        mockMvc.perform(post("/horses/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Hansi"))
                .andExpect(jsonPath("$.owner").value("Peter Pan"))
                .andExpect(jsonPath("$.consumptionList").isEmpty());
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = "Basic")
    void addNewHorse_AndExpectErrorMessage_400() throws Exception {
        String jsonString =
                """
                            {
                              "name": "",
                              "owner": "Peter Pan",
                              "consumptionList": []
                            }
                        """;
        mockMvc.perform(post("/horses/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andExpect(status().is(400))
                .andExpect(content().string("Feld \"Name\" darf nicht leer sein"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = "Basic")
    void deleteHorse_AndExpect_204() throws Exception {
        String jsonString =
                """
                            {
                              "name": "Hansi",
                              "owner": "Peter Pan",
                              "consumptionList": []
                            }
                        """;
        String postResponse = mockMvc.perform(post("/horses/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
        ).andReturn().getResponse().getContentAsString();


        String idToDelete = objectMapper.readValue(postResponse, Horse.class).id();

        mockMvc.perform(delete
                        ("/horses/" + idToDelete))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "Basic")
    void deleteHorse_AndExpect_404() throws Exception {
        mockMvc.perform(delete
                        ("/horses/1"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Kein Eintrag für die gegebene ID gefunden"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = "Basic")
    void putHorse_AndExpectErrorMessage_400() throws Exception {
        String jsonString =
                """
                            {
                              "name": "",
                              "owner": "Peter Pan",
                              "consumptionList": []
                            }
                        """;
        mockMvc.perform(put("/horses/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andExpect(status().is(400))
                .andExpect(content().string("Feld \"Name\" darf nicht leer sein"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = "Basic")
    void putHorse_AndExpect_201() throws Exception {
        String jsonString =
                """
                            {
                              "id": "1",
                              "name": "Hansi",
                              "owner": "Peter Pan",
                              "consumptionList": []
                            }
                        """;
        mockMvc.perform(put("/horses/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andExpect(status().is(201))
                .andExpect(content().json("""
                            {
                              "id": "1",
                              "name": "Hansi",
                              "owner": "Peter Pan",
                              "consumptionList": []
                            }
                        """));
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = "Basic")
    void putHorse_AndExpect_200() throws Exception {
        String jsonString =
                """
                            {
                              "id": "1",
                              "name": "Hansi",
                              "owner": "Peter Pan",
                              "consumptionList": []
                            }
                        """;
        String postResponse = mockMvc.perform(post("/horses/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)).andReturn().getResponse().getContentAsString();

        Horse createdHorse = objectMapper.readValue(postResponse, Horse.class);

        mockMvc.perform(put("/horses/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postResponse.replace("Hansi", "Lord Voldemort"))
                )
                .andExpect(status().is(200))
                .andExpect(content().json("""
                            {
                              "id": "<ID>",
                              "name": "Lord Voldemort",
                              "owner": "Peter Pan",
                              "consumptionList": []
                            }
                        """
                        .replace("<ID>", createdHorse.id())));
    }
}
