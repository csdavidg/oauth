package com.david.oauth.demo.oauthcommons.jwt;

import com.david.oauth.demo.oauthcommons.entity.Client;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.Random;

@AllArgsConstructor
public class JwtTokenGenerator {

    private static final long JWT_EXPIRATION = 10 * 60 * 60;

    private String jwtKey;

    private String generateRandomString() {
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public String generateAccessToken(Client client) {
        return Jwts.builder()
                .setSubject(client.getClientId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtKey).compact();
    }

    public String generateAuthorizationCode() {
        return this.generateRandomString().concat("ac");
    }

    public String generateState() {
        return this.generateRandomString().concat("st");
    }


}
