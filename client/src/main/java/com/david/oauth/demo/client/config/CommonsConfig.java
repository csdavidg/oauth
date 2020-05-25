package com.david.oauth.demo.client.config;

import com.david.oauth.demo.oauthcommons.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonsConfig {

    @Value("${jwt.secret}")
    private String jwtKey;

    @Bean
    public JwtTokenUtil jwtTokenUtil() {
        return new JwtTokenUtil(jwtKey);
    }

}
