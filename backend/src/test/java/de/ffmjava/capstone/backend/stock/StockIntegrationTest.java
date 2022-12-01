package de.ffmjava.capstone.backend.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
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
class StockIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "Basic")
    void getAllStockItems_AndExpectEmptyList_200() throws Exception {
        mockMvc.perform(get
                        ("/stock/"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = "Basic")
    void getItemById_AndExpectStockItem_200() throws Exception {
        String jsonString =
                """
                            {
                              "name": "Test",
                              "type": "Futter",
                              "amountInStock": 42.0,
                              "pricePerKilo": 42.0
                            }
                        """;
        String postResponse = mockMvc.perform(post("/stock/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)).andReturn().getResponse().getContentAsString();

        StockItem createdStockItem = objectMapper.readValue(postResponse, StockItem.class);

        mockMvc.perform(get
                        ("/stock//" + createdStockItem.id()))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":\"<ID>\",\"name\":\"Test\",\"type\":\"Futter\",\"amountInStock\":42.0,\"pricePerKilo\":42.0}"
                        .replace("<ID>", createdStockItem.id())));
    }


    @Test
    @DirtiesContext
    @WithMockUser(roles = "Basic")
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
        String postResponse = mockMvc.perform(post("/stock/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
        ).andReturn().getResponse().getContentAsString();


        String idToDelete = objectMapper.readValue(postResponse, StockItem.class).id();

        mockMvc.perform(delete
                        ("/stock//" + idToDelete))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "Basic")
    void deleteStockItem_AndExpect_404() throws Exception {
        mockMvc.perform(delete
                        ("/stock/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = "Basic")
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
        mockMvc.perform(post("/stock/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.type").isNotEmpty())
                .andExpect(jsonPath("$.amountInStock").isNotEmpty());


    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = "Basic")
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
        mockMvc.perform(post("/stock/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andExpect(status().is(400))
                .andExpect(content().string("Feld \"Name/Bezeichnung\" darf nicht leer sein"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = "Basic")
    void addNewStockItem_negativeAmountInStock_AndExpectErrorMessage_400() throws Exception {
        String jsonString =
                """
                            {
                              "name": "name",
                              "type": "Futter",
                              "amountInStock": -42.0,
                              "pricePerKilo": 42.0
                            }
                        """;
        mockMvc.perform(post("/stock/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andExpect(status().is(400))
                .andExpect(content().string("Der Wert muss größer als 0 sein"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = "Basic")
    void putStockItem_AndExpectErrorMessage_400() throws Exception {
        String jsonString =
                """
                            {
                              "id": "1",
                              "name": "",
                              "type": "Futter",
                              "amountInStock": 42.0,
                              "pricePerKilo": 42.0
                            }
                        """;
        mockMvc.perform(put("/stock/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andExpect(status().is(400))
                .andExpect(content().string("Feld \"Name/Bezeichnung\" darf nicht leer sein"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = "Basic")
    void putStockItem_AndExpect_201() throws Exception {
        String jsonString =
                """
                            {
                              "id": "1",
                              "name": "Test",
                              "type": "Futter",
                              "amountInStock": 42.0,
                              "pricePerKilo": 42.0
                            }
                        """;
        mockMvc.perform(put("/stock/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.name").isNotEmpty());
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = "Basic")
    void putStockItem_AndExpect_200() throws Exception {
        String jsonString =
                """
                            {
                              "name": "Test",
                              "type": "Futter",
                              "amountInStock": 42.0,
                              "pricePerKilo": 42.0
                            }
                        """;
        String postResponse = mockMvc.perform(post("/stock/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)).andReturn().getResponse().getContentAsString();

        StockItem createdStockItem = objectMapper.readValue(postResponse, StockItem.class);

        mockMvc.perform(put("/stock/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postResponse.replace("Futter", "Einstreu"))
                )
                .andExpect(status().is(200))
                .andExpect(content().string("{\"id\":\"<ID>\",\"name\":\"Test\",\"type\":\"Einstreu\",\"amountInStock\":42.0,\"pricePerKilo\":42.0}"
                        .replace("<ID>", createdStockItem.id())));
    }
}
