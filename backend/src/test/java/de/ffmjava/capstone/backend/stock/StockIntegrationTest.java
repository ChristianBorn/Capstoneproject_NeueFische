package de.ffmjava.capstone.backend.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
class StockIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllStockItems_AndExpectEmptyList_200() throws Exception {
        mockMvc.perform(get
                        ("/stock/overview"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DirtiesContext
    void deleteStockItem_AndExpect_204() throws Exception {
        String jsonString =
                """
                            {
                              "name": "Pellets",
                              "type": "Futter",
                              "amountInStock": 42.0,
                              "pricePerKilo": 42.0
                            }
                        """;
        mockMvc.perform(post("/stock/overview")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
        );

        String getResponse = mockMvc.perform(get
                        ("/stock/overview")).andReturn().getResponse().getContentAsString().replace("[", "")
                .replace("]", "");
        String idToDelete = objectMapper.readValue(getResponse, StockItem.class).id();

        mockMvc.perform(delete
                        ("/stock/overview/" + idToDelete))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteStockItem_AndExpect_404() throws Exception {
        mockMvc.perform(delete
                        ("/stock/overview/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void addNewStockItem_AndExpectSuccessMessage_201() throws Exception {
        String jsonString =
                """
                            {
                              "name": "Pellets",
                              "type": "Futter",
                              "amountInStock": 42.0,
                              "pricePerKilo": 42.0
                            }
                        """;
        mockMvc.perform(post("/stock/overview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andExpect(status().is(201))
                .andExpect(content().string("Neue Position \"Pellets\" erfolgreich gespeichert!"));

        mockMvc.perform(get("/stock/overview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..id").isNotEmpty())
                .andExpect(jsonPath("$..type").isNotEmpty())
                .andExpect(jsonPath("$..amountInStock").isNotEmpty());

    }

    @Test
    @DirtiesContext
    void addNewStockItem_AndExpectErrorMessage_400() throws Exception {
        String jsonString =
                """
                            {
                              "name": "",
                              "type": "Futter",
                              "amountInStock": 42.0,
                              "pricePerKilo": 42.0
                            }
                        """;
        mockMvc.perform(post("/stock/overview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andExpect(status().is(400))
                .andExpect(content().string("Feld \"Name/Bezeichnung\" darf nicht leer sein"));
    }
}
