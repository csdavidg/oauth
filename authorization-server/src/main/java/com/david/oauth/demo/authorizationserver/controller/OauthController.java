package com.david.oauth.demo.authorizationserver.controller;

import com.david.oauth.demo.authorizationserver.service.AuthorizationService;
import com.david.oauth.demo.authorizationserver.service.ClientManagement;
import com.david.oauth.demo.oauthcommons.entity.Client;
import com.david.oauth.demo.oauthcommons.entity.ResponseToken;
import com.david.oauth.demo.oauthcommons.enums.ResponseTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.david.oauth.demo.oauthcommons.constants.Constants.ACCESS_TOKEN;
import static com.david.oauth.demo.oauthcommons.constants.Constants.ALGORITHM;
import static com.david.oauth.demo.oauthcommons.constants.Constants.CLIENT_ID;
import static com.david.oauth.demo.oauthcommons.constants.Constants.CODE_CHALLENGE;
import static com.david.oauth.demo.oauthcommons.constants.Constants.CODE_VERIFIER;
import static com.david.oauth.demo.oauthcommons.constants.Constants.REDIRECT_URI;
import static com.david.oauth.demo.oauthcommons.constants.Constants.REFRESH_TOKEN;
import static com.david.oauth.demo.oauthcommons.constants.Constants.RESPONSE_TYPE;
import static com.david.oauth.demo.oauthcommons.constants.Constants.STATE;

@RestController
@RequestMapping("/oauth")
public class OauthController {

    private final ClientManagement clientService;

    private final AuthorizationService authorizationService;

    @Autowired
    public OauthController(ClientManagement clientService, AuthorizationService authorizationService) {
        this.clientService = clientService;
        this.authorizationService = authorizationService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }

    @GetMapping("/authorize")
    public RedirectView getAuthorizationCode(@RequestParam(RESPONSE_TYPE) String responseType,
                                             @RequestParam(STATE) String state,
                                             @RequestParam(CLIENT_ID) String clientId,
                                             @RequestParam(REDIRECT_URI) String redirectUri,
                                             @RequestParam(CODE_CHALLENGE) String codeChallenge,
                                             @RequestParam(ALGORITHM) String algorithm) {

        RedirectView redirect = new RedirectView(redirectUri);
        try {
            Client client = clientService.validateOauthClient(clientId, redirectUri, responseType);
            clientService.saveCodeChallengeAndAlgorithm(codeChallenge, algorithm);
            Map<String, String> attributesMap = new HashMap<>();
            attributesMap.put(ResponseTypeEnum.CODE.getType(), authorizationService.generateAuthorizationCodeForClient(client, state));
            attributesMap.put("state", state);
            redirect.setStatusCode(HttpStatus.FOUND);
            redirect.setAttributesMap(attributesMap);
        } catch (Exception e) {
            redirect.setStatusCode(HttpStatus.BAD_REQUEST);
        }
        return redirect;
    }

    @PostMapping("/token")
    public ResponseEntity<?> getToken(HttpServletRequest request) {
        try {
            Client client = clientService.validateOauthClient(request);
            ResponseToken responseToken = authorizationService.getAccessToken(client, request);
            return new ResponseEntity<>(responseToken, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/revoke/access/token")
    public ResponseEntity<?> deleteAccessToken(@RequestParam("client_id") String clientId) {
        try {
            authorizationService.revokeToken(clientId.concat(ACCESS_TOKEN));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/revoke/refresh/token")
    public ResponseEntity<?> deleteRefreshToken(@RequestParam("client_id") String clientId) {
        try {
            authorizationService.revokeToken(clientId.concat(REFRESH_TOKEN));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
