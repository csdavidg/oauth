package com.david.oauth.demo.client.authorizationserver.service;

import com.david.oauth.demo.client.authorizationserver.component.JwtTokenGenerator;
import com.david.oauth.demo.client.authorizationserver.entity.ResponseToken;
import com.david.oauth.demo.client.authorizationserver.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private JwtTokenGenerator jwtTokenGenerator;

    private ClientManagement clientService;

    @Autowired
    public AuthorizationService(JwtTokenGenerator jwtTokenGenerator, ClientManagement clientService) {
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.clientService = clientService;
    }

    public String generateAuthorizationCodeForClient(Client client, String state) {
        String authorizationCode = jwtTokenGenerator.generateAuthorizationCode();
        client.setAuthorizationCode(authorizationCode);
        client.setState(state);
        clientService.save(client);
        return authorizationCode;
    }

    public ResponseToken createResponseAccessToken(Client client) {
        return ResponseToken.builder()
                .accessToken(jwtTokenGenerator.generateAccessToken(client))
                .tokenType("Bearer")
                .state(client.getState())
                .build();
    }
}
