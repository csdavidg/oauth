package com.david.oauth.demo.authorizationserver.service;

import com.david.oauth.demo.oauthcommons.entity.Client;
import com.david.oauth.demo.oauthcommons.entity.ResponseToken;
import com.david.oauth.demo.oauthcommons.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private JwtTokenUtil jwtTokenUtil;

    private ClientManagement clientService;

    @Autowired
    public AuthorizationService(JwtTokenUtil jwtTokenUtil, ClientManagement clientService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.clientService = clientService;
    }

    public String generateAuthorizationCodeForClient(Client client, String state) {
        String authorizationCode = jwtTokenUtil.generateAuthorizationCode();
        client.setAuthorizationCode(authorizationCode);
        client.setState(state);
        clientService.save(client);
        return authorizationCode;
    }

    public ResponseToken createResponseAccessToken(Client client) {
        return ResponseToken.builder()
                .accessToken(jwtTokenUtil.generateAccessToken(client))
                .tokenType("Bearer")
                .state(client.getState())
                .build();
    }
}
