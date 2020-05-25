package com.david.oauth.demo.oauthcommons.util;

import com.david.oauth.demo.oauthcommons.entity.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.joda.time.DateTime;

import javax.xml.bind.DatatypeConverter;
import java.util.Random;

@AllArgsConstructor
public class JwtTokenUtil {

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

    public String generateAuthorizationCode() {
        return this.generateRandomString().concat("ac");
    }

    public String generateState() {
        return this.generateRandomString().concat("st");
    }

    public String generateAccessToken(Client client) {
        try {
            return Jwts.builder()
                    .setSubject(new ObjectMapper().writeValueAsString(client))
                    .setIssuedAt(new DateTime().toDate())
                    .setExpiration(new DateTime().plusHours(2).toDate())
                    .signWith(SignatureAlgorithm.HS512, jwtKey).compact();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error generating access token " + e.getMessage());
        }
    }

    public Claims validateJwtAccessToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(this.jwtKey))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error validating JWT " + e.getMessage());
        }
    }


}
