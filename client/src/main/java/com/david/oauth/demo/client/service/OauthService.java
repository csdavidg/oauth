package com.david.oauth.demo.client.service;

import com.david.oauth.demo.client.config.OauthConfig;
import com.david.oauth.demo.oauthcommons.entity.ResponseToken;
import com.david.oauth.demo.oauthcommons.enums.GrantTypeEnum;
import com.david.oauth.demo.oauthcommons.enums.ResponseTypeEnum;
import com.david.oauth.demo.oauthcommons.managers.KeyStoreManager;
import com.david.oauth.demo.oauthcommons.util.JwtTokenUtil;
import com.david.oauth.demo.oauthcommons.util.PKCEUtil;
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
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.david.oauth.demo.oauthcommons.constants.Constants.ACCESS_TOKEN;
import static com.david.oauth.demo.oauthcommons.constants.Constants.ALGORITHM;
import static com.david.oauth.demo.oauthcommons.constants.Constants.AUTHORIZATION_CODE;
import static com.david.oauth.demo.oauthcommons.constants.Constants.CLIENT_ID;
import static com.david.oauth.demo.oauthcommons.constants.Constants.CODE;
import static com.david.oauth.demo.oauthcommons.constants.Constants.CODE_CHALLENGE;
import static com.david.oauth.demo.oauthcommons.constants.Constants.CODE_VERIFIER;
import static com.david.oauth.demo.oauthcommons.constants.Constants.GRANT_TYPE;
import static com.david.oauth.demo.oauthcommons.constants.Constants.REDIRECT_URI;
import static com.david.oauth.demo.oauthcommons.constants.Constants.REFRESH_TOKEN;
import static com.david.oauth.demo.oauthcommons.constants.Constants.RESPONSE_TYPE;
import static com.david.oauth.demo.oauthcommons.constants.Constants.STATE;

@Service
public class OauthService {

    private static final String AS_AUTHORIZATION_CODE = "as_authorization_code";
    private static final String AS_TOKEN = "as_token";
    private static final String HASH_ALGORITHM = "SHA-256";

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
        String codeVerifier = jwtTokenUtil.generateRandomString();
        keyStoreManager.saveValueIntoKeyStore(CODE_VERIFIER, codeVerifier);

        String codeChallenge = new PKCEUtil(HASH_ALGORITHM).getCodeChallenge(codeVerifier);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(RESPONSE_TYPE, responseType.getType());
        params.add(STATE, createAndSaveRequestState());
        params.add(CLIENT_ID, oauthConfig.getClient());
        params.add(REDIRECT_URI, oauthConfig.getCallback());
        params.add(CODE_CHALLENGE, Base64.getEncoder().encodeToString(codeChallenge.getBytes()));
        params.add(ALGORITHM, HASH_ALGORITHM);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(oauthConfig.getNodes().get(AS_AUTHORIZATION_CODE))
                .queryParams(params);

        return uriBuilder.toUriString();
    }

    public ResponseToken getAccessTokenUsingCode() {
        MultiValueMap<String, String> requestBody = createAccessTokenRequestBody(GrantTypeEnum.AUTHORIZATION_CODE);
        requestBody.add(CODE, keyStoreManager.getValueFromKeyStore(AUTHORIZATION_CODE));
        requestBody.add(CODE_VERIFIER, keyStoreManager.getValueFromKeyStore(CODE_VERIFIER));
        return getTokenFromAuthorizationServer(requestBody);
    }

    public ResponseToken getAccessTokenUsingRefresh() throws IOException {
        String valueFromKey = keyStoreManager.getValueFromKeyStore(REFRESH_TOKEN);
        if (valueFromKey == null) {
            throw new IllegalArgumentException("Invalid Refresh Token");
        }
        ResponseToken responseToken = new ObjectMapper().readValue(valueFromKey, ResponseToken.class);

        MultiValueMap<String, String> requestBody = createAccessTokenRequestBody(GrantTypeEnum.REFRESH_TOKEN);
        requestBody.add(REFRESH_TOKEN, responseToken.getRefreshToken());
        return getTokenFromAuthorizationServer(requestBody);
    }

    public ResponseToken getAccessTokenUsingCredentials() {
        MultiValueMap<String, String> requestBody = createAccessTokenRequestBody(GrantTypeEnum.CLIENT_CREDENTIALS);
        return getTokenFromAuthorizationServer(requestBody);
    }

    public void validateAndSaveAuthorizationCode(String code, String state) {
        if (!keyStoreManager.getValueFromKeyStore(STATE).equals(state)) {
            throw new IllegalArgumentException();
        }
        keyStoreManager.saveValueIntoKeyStore(AUTHORIZATION_CODE, code);
    }

    public String createAndSaveRequestState() {
        String state = jwtTokenUtil.generateState();
        keyStoreManager.saveValueIntoKeyStore(STATE, state);
        return state;
    }

    public ResponseToken getAccessTokenFromKeyStore() throws IOException {
        String valueFromKey = keyStoreManager.getValueFromKeyStore(ACCESS_TOKEN);
        if (valueFromKey == null) {
            throw new IllegalArgumentException("Invalid Access Token");
        }
        return new ObjectMapper().readValue(valueFromKey, ResponseToken.class);
    }

    public void revokeAccessToken() {
        keyStoreManager.deleteValueInKeyStore(ACCESS_TOKEN);
    }

    private MultiValueMap<String, String> createAccessTokenRequestBody(GrantTypeEnum grantType) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(GRANT_TYPE, grantType.getType());
        body.add(REDIRECT_URI, oauthConfig.getCallback());
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
            keyStoreManager.saveValueIntoKeyStore(ACCESS_TOKEN, jsonToken);

            if (responseToken != null && responseToken.getRefreshToken() != null) {
                responseToken.setAccessToken(null);
                jsonToken = new ObjectMapper().writeValueAsString(responseToken);
                keyStoreManager.saveValueIntoKeyStore(REFRESH_TOKEN, jsonToken);
            }

        } catch (RestClientResponseException | IllegalArgumentException | IOException e) {
            throw new IllegalArgumentException("Error getting token " + e.getMessage());
        }
        return responseToken;
    }

}
