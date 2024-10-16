package com.example.demo.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class TokenAPITests {
    @Autowired  MockMvc mvc;

    @Test
    void AccessTokenWithoutAuth() throws Exception{

        MvcResult result = mvc.perform(get("/token")).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals("fake token", response);

    }

    @Test
    void AccessTokenwithAuth() throws Exception{
       
        MvcResult result =
                mvc.perform(
                    post("/token")
                    .with(httpBasic("ApiClientApp", "secret"))
                )
                .andExpect(status().isOk())
                .andReturn();

                String encodedToken = result.getResponse().getContentAsString();

    }

}
