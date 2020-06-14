package com.david.oauth.demo.client.service;

import com.david.oauth.demo.client.config.OauthConfig;
import com.david.oauth.demo.oauthcommons.entity.ResponseToken;
import com.david.oauth.demo.oauthcommons.enums.GrantTypeEnum;
import com.david.oauth.demo.oauthcommons.enums.ResponseTypeEnum;
import com.david.oauth.demo.oauthcommons.managers.KeyStoreManager;
import com.david.oauth.demo.oauthcommons.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.io.IOException;

import static com.david.oauth.demo.oauthcommons.constants.Constants.KEY_STORE_ALIAS_ACCESS_TOKEN;
import static com.david.oauth.demo.oauthcommons.constants.Constants.KEY_STORE_ALIAS_AUTHORIZATION_CODE;
import static com.david.oauth.demo.oauthcommons.constants.Constants.KEY_STORE_ALIAS_REFRESH_TOKEN;
import static com.david.oauth.demo.oauthcommons.constants.Constants.KEY_STORE_ALIAS_STATE;

@Service
public class OauthService {

    private static final String AS_AUTHORIZATION_CODE = "as_authorization_code";
    private static final String AS_TOKEN = "as_token";


    @Resource
    private OauthConfig oauthConfig;

    private final KeyStoreManager keyStoreManager;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public OauthService(KeyStoreManager tokenService, JwtTokenUtil jwtTokenUtil) {
        this.keyStoreManager = tokenService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public String getAuthorizationCodeURI(ResponseTypeEnum responseType) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("response_type", responseType.getType());
        params.add("state", createAndSaveRequestState());
        params.add("client_id", oauthConfig.getClient());
        params.add("redirect_uri", oauthConfig.getCallback());

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(oauthConfig.getNodes().get(AS_AUTHORIZATION_CODE))
                .queryParams(params);

        return uriBuilder.toUriString();
    }

    public ResponseToken getAccessTokenUsingCode() {
        String authorizationCode = keyStoreManager.getValueFromKeyStore(KEY_STORE_ALIAS_AUTHORIZATION_CODE);
        if (authorizationCode == null) {
            throw new IllegalArgumentException("Invalid Authorization Code");
        }
        MultiValueMap<String, String> requestBody = createAccessTokenRequestBody(GrantTypeEnum.AUTHORIZATION_CODE);
        requestBody.add("code", authorizationCode);
        return getTokenFromAuthorizationServer(requestBody);
    }

    public ResponseToken getAccessTokenUsingRefresh() throws IOException {
        String valueFromKey = keyStoreManager.getValueFromKeyStore(KEY_STORE_ALIAS_REFRESH_TOKEN);
        if (valueFromKey == null) {
            throw new IllegalArgumentException("Invalid Refresh Token");
        }
        ResponseToken responseToken = new ObjectMapper().readValue(valueFromKey, ResponseToken.class);

        MultiValueMap<String, String> requestBody = createAccessTokenRequestBody(GrantTypeEnum.REFRESH_TOKEN);
        requestBody.add("refresh_token", responseToken.getRefreshToken());
        return getTokenFromAuthorizationServer(requestBody);
    }

    public ResponseToken getAccessTokenUsingCredentials() {
        MultiValueMap<String, String> requestBody = createAccessTokenRequestBody(GrantTypeEnum.CLIENT_CREDENTIALS);
        return getTokenFromAuthorizationServer(requestBody);
    }

    public void validateAndSaveAuthorizationCode(String code, String state) {
        if (!keyStoreManager.getValueFromKeyStore(KEY_STORE_ALIAS_STATE).equals(state)) {
            throw new IllegalArgumentException();
        }
        keyStoreManager.saveValueIntoKeyStore(KEY_STORE_ALIAS_AUTHORIZATION_CODE, code);
    }

    public String createAndSaveRequestState() {
        String state = jwtTokenUtil.generateState();
        keyStoreManager.saveValueIntoKeyStore(KEY_STORE_ALIAS_STATE, state);
        return state;
    }

    public ResponseToken getAccessTokenFromKeyStore() throws IOException {
        String valueFromKey = keyStoreManager.getValueFromKeyStore(KEY_STORE_ALIAS_ACCESS_TOKEN);
        if (valueFromKey == null) {
            throw new IllegalArgumentException("Invalid Access Token");
        }
        return new ObjectMapper().readValue(valueFromKey, ResponseToken.class);
    }

    public void revokeAccessToken() {
        keyStoreManager.deleteValueInKeyStore(KEY_STORE_ALIAS_ACCESS_TOKEN);
    }

    private MultiValueMap<String, String> createAccessTokenRequestBody(GrantTypeEnum grantType) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", grantType.getType());
        body.add("redirect_uri", oauthConfig.getCallback());
        return body;
    }

    private ResponseToken getTokenFromAuthorizationServer(MultiValueMap<String, String> body) {

        ResponseToken responseToken;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth(oauthConfig.getClient(), oauthConfig.getSecret());

            HttpEntity<?> request = new HttpEntity<>(body, headers);
            ResponseEntity<ResponseToken> response = new RestTemplate().exchange(oauthConfig.getNodes().get(AS_TOKEN),
                    HttpMethod.POST, request, ResponseToken.class);
            responseToken = response.getBody();

            String jsonToken = new ObjectMapper().writeValueAsString(responseToken);
            keyStoreManager.saveValueIntoKeyStore(KEY_STORE_ALIAS_ACCESS_TOKEN, jsonToken);

            if (responseToken != null && responseToken.getRefreshToken() != null) {
                responseToken.setAccessToken(null);
                jsonToken = new ObjectMapper().writeValueAsString(responseToken);
                keyStoreManager.saveValueIntoKeyStore(KEY_STORE_ALIAS_REFRESH_TOKEN, jsonToken);
            }

        } catch (RestClientResponseException | IllegalArgumentException | IOException e) {
            throw new IllegalArgumentException("Error getting token " + e.getMessage());
        }
        return responseToken;
    }

}
