package com.david.oauth.demo.oauthcommons.util;

import com.david.oauth.demo.oauthcommons.entity.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JwtTokenTest {

    private static final String jwtKey = "jwtkeytest";

    private JwtTokenUtil jwtTokenUtil;

    private Client client;

    @BeforeEach
    public void setUp() {
        this.jwtTokenUtil = new JwtTokenUtil(jwtKey);
        client = Client.builder()
                .id(1L)
                .clientId("client_test")
                .clientSecret("client_secret_test")
                .build();
    }

    @Test
    public void validateAuthorizationCode() {
        String authorizationCode = jwtTokenUtil.generateAuthorizationCode();
        assertEquals(12, authorizationCode.length());
        assertTrue(authorizationCode.endsWith("ac"));
    }

    @Test
    public void validateState() {
        String state = jwtTokenUtil.generateState();
        assertEquals(12, state.length());
        assertTrue(state.endsWith("st"));
    }

    @Test
    public void validateAccessTokenGeneration() {
        try {
            String clientJson = new ObjectMapper().writeValueAsString(client);
            String token = jwtTokenUtil.generateAccessToken(client);
            Claims claim = jwtTokenUtil.validateJwtAccessToken(token);
            assertEquals(clientJson, claim.getSubject());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
