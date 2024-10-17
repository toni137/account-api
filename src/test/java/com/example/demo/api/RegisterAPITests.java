package com.example.demo.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.repository.CustomerRepository;

@WebMvcTest(RegisterAPI.class)
public class RegisterAPITests {

    @Autowired  MockMvc mvc;

    @MockBean
    private CustomerRepository repo;

     @Test
    void testPostRegister() throws Exception {
        // Create customer JSON
        String customerJson = "{\"name\":\"test\", \"email\":\"email@email.com\", \"password\":\"pass\"}";

        // Perform POST request to register customer
        mvc.perform(post("/register")
                .content(customerJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }


}
