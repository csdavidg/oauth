package com.david.oauth.demo.client.service;

import com.david.oauth.demo.client.config.OauthConfig;
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

    @Resource
    private OauthConfig oauthConfig;

    Logger logger = LoggerFactory.getLogger(OauthService.class);

    public String getToken(String code) throws JSONException {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://localhost:8081/oauth/access_token";
        //(client_id=${client}, client_secret, redirect_uri=${callback}

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> oauthPayload = new LinkedMultiValueMap<String, String>();
        oauthPayload.add("client_id", oauthConfig.getClient());
        oauthPayload.add("client_secret", oauthConfig.getSecret());
        oauthPayload.add("code", code);
        oauthPayload.add("redirect_uri", oauthConfig.getCallback());

        HttpEntity<?> request = new HttpEntity<>(oauthPayload, headers);
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.POST, request, Object.class);

        JSONObject jsonObject = new JSONObject(response.getBody().toString());

        logger.debug("ACCES TOKEN " + jsonObject.get("access_token"));

        return "TOKEN";


    }


}
