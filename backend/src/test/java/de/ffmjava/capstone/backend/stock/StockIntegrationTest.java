package de.ffmjava.capstone.backend.stock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}
