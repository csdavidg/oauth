package com.david.oauth.demo.client.authorizationserver.component;

import com.david.oauth.demo.client.authorizationserver.entity.Client;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

@Component
public class JwtTokenGenerator {

    private static final long JWT_EXPIRATION = 10 * 60 * 60;

    @Value("${jwt.secret}")
    private String jwtKey;


    public String generateAuthorizationCode() {
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


}
