package de.ffmjava.capstone.backend.horses;

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
class HorseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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
                              "consumption": []
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
                              "consumption": []
                            }
                        """;
        mockMvc.perform(post("/horses/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andExpect(status().is(400))
                .andExpect(content().string("Feld \"Name\" darf nicht leer sein"));
    }
}
