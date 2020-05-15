package com.david.oauth.demo.client.authorizationserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "oauth")
@Getter
@Setter
public class ClientConfig {

    private Map<String, String> clients;
}
