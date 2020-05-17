package com.david.oauth.demo.client.service;

import com.david.oauth.demo.authorizationserver.enums.GrantTypeEnum;
import com.david.oauth.demo.client.config.OauthConfig;
import com.david.oauth.demo.oauthcommons.entity.ResponseToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Service
public class OauthService {

    private static final String AUTHORIZATION_SERVER = "authorization-server";

    @Resource
    private OauthConfig oauthConfig;

    public ResponseToken getToken(String code, String originalState, String state) {
        try {

            if (!originalState.equals(state)) {
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
            return response.getBody();
        } catch (RestClientResponseException | IllegalArgumentException e) {
            ResponseToken responseToken = new ResponseToken();
            responseToken.setMessage(e.getMessage());
            return responseToken;
        }
    }

}
