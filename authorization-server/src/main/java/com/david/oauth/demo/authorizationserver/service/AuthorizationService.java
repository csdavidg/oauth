package com.david.oauth.demo.authorizationserver.service;

import com.david.oauth.demo.oauthcommons.entity.Client;
import com.david.oauth.demo.oauthcommons.entity.ResponseToken;
import com.david.oauth.demo.oauthcommons.enums.GrantTypeEnum;
import com.david.oauth.demo.oauthcommons.managers.KeyStoreManager;
import com.david.oauth.demo.oauthcommons.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.david.oauth.demo.oauthcommons.constants.Constants.GRANT_TYPE;
import static com.david.oauth.demo.oauthcommons.constants.Constants.ACCESS_TOKEN;
import static com.david.oauth.demo.oauthcommons.constants.Constants.REFRESH_TOKEN;
import static com.david.oauth.demo.oauthcommons.constants.Constants.TOKEN_TYPE;

@Service
public class AuthorizationService {

    private final JwtTokenUtil jwtTokenUtil;

    private final ClientManagement clientService;

    private final KeyStoreManager keyStoreManager;

    @Autowired
    public AuthorizationService(JwtTokenUtil jwtTokenUtil, ClientManagement clientService, KeyStoreManager keyStoreManager) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.clientService = clientService;
        this.keyStoreManager = keyStoreManager;
    }

    public String generateAuthorizationCodeForClient(Client client, String state) {
        String authorizationCode = jwtTokenUtil.generateAuthorizationCode();
        client.setAuthorizationCode(authorizationCode);
        client.setState(state);
        clientService.save(client);
        return authorizationCode;
    }

    public ResponseToken getAccessToken(Client client, HttpServletRequest request) {

        GrantTypeEnum grantType = GrantTypeEnum.getValueFromString(request.getParameter(GRANT_TYPE));

        if (grantType.equals(GrantTypeEnum.AUTHORIZATION_CODE)) {

            String refreshToken = jwtTokenUtil.generateRefreshToken();
            keyStoreManager.saveValueIntoKeyStore(client.getClientId().concat(REFRESH_TOKEN), refreshToken);
            return createResponseAccessToken(client, refreshToken);
        } else if (grantType.equals(GrantTypeEnum.REFRESH_TOKEN)) {

            String refreshTokenFromKeyStore = keyStoreManager.getValueFromKeyStore(client.getClientId().concat(REFRESH_TOKEN));
            String refreshToken = request.getParameter("refresh_token");
            if (refreshTokenFromKeyStore != null && refreshTokenFromKeyStore.equals(refreshToken)) {
                jwtTokenUtil.validateJwtAccessToken(refreshToken);
                return createResponseAccessToken(client, refreshTokenFromKeyStore);
            }
        } else if (grantType.equals(GrantTypeEnum.CLIENT_CREDENTIALS)) {

            return createResponseAccessToken(client, null);
        }


        throw new IllegalArgumentException();
    }

    public void revokeToken(String alias) {
        keyStoreManager.deleteValueInKeyStore(alias);
    }

    private ResponseToken createResponseAccessToken(Client client, String refreshToken) {
        String accessToken = jwtTokenUtil.generateAccessToken(client);
        keyStoreManager.saveValueIntoKeyStore(client.getClientId().concat(ACCESS_TOKEN), accessToken);
        return ResponseToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(TOKEN_TYPE)
                .state(client.getState())
                .build();
    }
}
