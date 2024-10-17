package com.example.demo.service;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class TokenService {

    public static String generateAccessToken(String name) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            Date expireDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 4);
            return JWT.create()
            .withSubject(name)
            .withClaim("name", name)
            .withExpiresAt(expireDate)
            .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new JWTCreationException("Error while generating token", e);
        }
    }

    public static boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier =  JWT.require(algorithm)
            .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    
    
    
}
