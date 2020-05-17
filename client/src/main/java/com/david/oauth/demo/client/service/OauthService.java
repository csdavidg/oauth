package com.david.oauth.demo.client.service;

import com.david.oauth.demo.client.config.OauthConfig;
import com.david.oauth.demo.client.entity.ResponseToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONException;
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

    Logger logger = LoggerFactory.getLogger(OauthService.class);

    public ResponseToken getToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(oauthConfig.getClient(), oauthConfig.getSecret());

        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", oauthConfig.getCallback());

        try {
            HttpEntity<?> request = new HttpEntity<>(body, headers);
            ResponseEntity<ResponseToken> response = new RestTemplate().exchange(oauthConfig.getNodes().get(AUTHORIZATION_SERVER),
                    HttpMethod.POST, request, ResponseToken.class);
            return response.getBody();
        } catch (RestClientResponseException re) {
            ResponseToken responseToken = new ResponseToken();
            responseToken.setMessage(re.getMessage());
            return responseToken;
        }
    }


}
