package com.david.oauth.demo.client.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "oauth")
@Getter
@Setter
public class OauthConfig {

    private String client;
    private String secret;
    private String callback;
    private Map<String, String> nodes;

}
