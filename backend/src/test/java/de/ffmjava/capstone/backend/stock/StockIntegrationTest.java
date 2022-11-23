package de.ffmjava.capstone.backend.stock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
class StockIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllStockItems_AndExpectEmptyList_200() throws Exception {
        mockMvc.perform(get
                        ("/lager/ueberblick"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
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
        mockMvc.perform(post("/lager/ueberblick")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andExpect(status().is(201))
                .andExpect(content().string("Neue Position \"Pellets\" erfolgreich gespeichert!"));


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
        mockMvc.perform(post("/lager/ueberblick")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andExpect(status().is(400))
                .andExpect(content().string("Feld \"Name/Bezeichnung\" darf nicht leer sein"));



    }
}
