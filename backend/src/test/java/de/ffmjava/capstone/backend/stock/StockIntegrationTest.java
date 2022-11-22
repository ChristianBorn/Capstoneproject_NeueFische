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

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllStockItems_AndExpectEmptyList_200() throws Exception {
        mockMvc.perform(get
                        ("/lager/ueberblick"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DirtiesContext
    void addNewStockItem_AndExpectStockItem_201() throws Exception {
        String jsonString =
                """
                    {
                      "name": "Pellets",
                      "type": "Futter",
                      "amountInStock": 42.0,
                      "pricePerKilo": 42.0
                    }
                """;
        String apiResponse = mockMvc.perform(post("/lager/ueberblick")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        StockItem createdItem = objectMapper.readValue(apiResponse, StockItem.class);
        String returnedJson = """
                    [{
                      "id": <ID>,
                      "name": "Pellets",
                      "type": "Futter",
                      "amountInStock": 42.0,
                      "pricePerKilo": 42.0
                    }]
                """.replace("<ID>", createdItem.id());

        mockMvc.perform(get("/lager/ueberblick"))
                .andExpect(status().isOk())
                .andExpect(content().json(returnedJson));
    }
}
