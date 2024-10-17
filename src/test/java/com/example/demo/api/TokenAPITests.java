package com.example.demo.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.domain.Token;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.TokenService;

@WebMvcTest(TokenAPI.class)
public class TokenAPITests {
    @Autowired  MockMvc mvc;

    @MockBean
    private CustomerRepository repo;

    @MockBean
    private TokenService tokenService;

    @Test
    void AccessServiceAPI() throws Exception{

        MvcResult result = mvc.perform(get("/token")).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals("fake token", response);

    }

    @Test
    void testCreateTokenForCustomerUnauthorized() throws Exception {
        // Create customer JSON with incorrect credentials
        String customerJson = "{\"name\":\"wrong_name\", \"password\":\"wrong_password\"}";

        // Perform POST request to create token for customer
        mvc.perform(post("/token")
                .content(customerJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCreateTokenForCustomerAuthorized() throws Exception {
        // Create customer JSON with correct credentials
        String customerJson = "{\"name\":\"ApiClientApp\", \"password\":\"secret\"}";

        // Perform POST request to create token for customer
        mvc.perform(post("/token")
                .content(customerJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testTokenisValid() throws Exception {
        // Create customer JSON with correct credentials
        String customerJson = "{\"name\":\"ApiClientApp\", \"password\":\"secret\"}";

        // Perform POST request to create token for customer
        MvcResult result = mvc.perform(post("/token")
                .content(customerJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        String encodedToken = responseContent.replace("{\"token\":\"", "").replace("\"}", "");

        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm)
            .build();
            DecodedJWT jwt = verifier.verify(encodedToken);

            assertEquals("ApiClientApp", jwt.getSubject());
            
        } catch (JWTVerificationException e) {
            throw new Exception(e);
        }

    }


    
}