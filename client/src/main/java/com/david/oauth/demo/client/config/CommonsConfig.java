package com.david.oauth.demo.client.config;

import com.david.oauth.demo.oauthcommons.managers.KeyStoreManager;
import com.david.oauth.demo.oauthcommons.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonsConfig {

    @Value("${jwt.secret}")
    private String jwtKey;

    @Value("${oauth.key-store.name}")
    public String keyStoreName;

    @Value("${oauth.key-store.password}")
    public String keyStorePassword;

    @Bean
    public JwtTokenUtil jwtTokenUtil() {
        return new JwtTokenUtil(jwtKey);
    }

    @Bean
    public KeyStoreManager tokenService() {
        return new KeyStoreManager(keyStoreName, keyStorePassword);
    }

}
