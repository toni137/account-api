package com.example.demo.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class TokenAPITests {
    @Autowired
    MockMvc mvc;

     @Test
    //@Disabled
    void tokenWhenUnauthenticatedThen401() throws Exception {
        this.mvc.perform(get("http://localhost:8081/account/token"))
                .andExpect(status().isUnauthorized());
    }
}
