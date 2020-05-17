package com.david.oauth.demo.client.service;

import com.david.oauth.demo.client.config.OauthConfig;
import com.david.oauth.demo.client.entity.ResponseToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
public class OauthService {

    private static final String AUTHORIZATION_SERVER = "authorization-server";

    @Resource
    private OauthConfig oauthConfig;

    Logger logger = LoggerFactory.getLogger(OauthService.class);

    public String getToken(String code) throws JSONException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(oauthConfig.getClient(), oauthConfig.getSecret());

        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", oauthConfig.getCallback());

        HttpEntity<?> request = new HttpEntity<>(body, headers);
        ResponseEntity<ResponseToken> response = restTemplate.exchange(oauthConfig.getNodes().get(AUTHORIZATION_SERVER),
                HttpMethod.POST, request, ResponseToken.class);

//        JSONObject jsonObject = new JSONObject(response.getBody().toString());
//
//        logger.debug("ACCES TOKEN " + jsonObject.get("access_token"));

        return "TOKEN";


    }


}
