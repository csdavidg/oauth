package com.david.oauth.demo.authorizationserver.config;

import com.david.oauth.demo.oauthcommons.jwt.JwtTokenGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonsConfig {

    @Value("${jwt.secret}")
    private String jwtKey;

    @Bean
    public JwtTokenGenerator jwtTokenGenerator() {
        return new JwtTokenGenerator(jwtKey);
    }

}
