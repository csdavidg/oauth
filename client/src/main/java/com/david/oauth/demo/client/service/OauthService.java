package com.david.oauth.demo.client.service;

import com.david.oauth.demo.authorizationserver.enums.GrantTypeEnum;
import com.david.oauth.demo.client.config.OauthConfig;
import com.david.oauth.demo.oauthcommons.entity.ResponseToken;
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

import javax.annotation.Resource;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;

import static com.david.oauth.demo.oauthcommons.constants.Constants.KEY_STORE_ALIAS_ACCESS_TOKEN;
import static com.david.oauth.demo.oauthcommons.constants.Constants.KEY_STORE_ALIAS_STATE;

@Service
public class OauthService {

    private static final String AUTHORIZATION_SERVER = "authorization-server";

    @Resource
    private OauthConfig oauthConfig;

    private TokenService tokenService;

    @Autowired
    public OauthService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public ResponseToken getToken(String code, String state) {
        ResponseToken responseToken = new ResponseToken();

        try {
            if (!this.tokenService.getValueFromKeyStore(KEY_STORE_ALIAS_STATE).equals(state)) {
                throw new IllegalArgumentException();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth(oauthConfig.getClient(), oauthConfig.getSecret());

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", GrantTypeEnum.AUTHORIZATION_CODE.getType());
            body.add("code", code);
            body.add("redirect_uri", oauthConfig.getCallback());

            HttpEntity<?> request = new HttpEntity<>(body, headers);
            ResponseEntity<ResponseToken> response = new RestTemplate().exchange(oauthConfig.getNodes().get(AUTHORIZATION_SERVER),
                    HttpMethod.POST, request, ResponseToken.class);
            responseToken = response.getBody();

            String jsonToken = new ObjectMapper().writeValueAsString(responseToken);
            this.tokenService.saveValueIntoKeyStore(KEY_STORE_ALIAS_ACCESS_TOKEN, jsonToken);

            String valueFromKey = this.tokenService.getValueFromKeyStore(KEY_STORE_ALIAS_ACCESS_TOKEN);
            System.out.println("JSON: " + valueFromKey);
        } catch (RestClientResponseException | IllegalArgumentException | NoSuchAlgorithmException | UnrecoverableEntryException | KeyStoreException | IOException e) {
            responseToken.setMessage(e.getMessage());
        }
        return responseToken;
    }

    public String getProtected() {
        return "";
    }

}
