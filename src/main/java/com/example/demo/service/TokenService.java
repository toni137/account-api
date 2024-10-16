package com.example.demo.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class TokenService {

    public String generateAccessToken(String name) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            return JWT.create()
            .withSubject(name)
            .withClaim("name", name)
            .withExpiresAt(genAccessExpirationDate())
            .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new JWTCreationException("Error while generating token", e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            return JWT.require(algorithm)
            .build().verify(token)
            .getSubject();
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Error while validating token", e);
        }
    }

    private Date genAccessExpirationDate(){
        return new Date(System.currentTimeMillis() + 14400000); //current time + 4h
    }
    
}
