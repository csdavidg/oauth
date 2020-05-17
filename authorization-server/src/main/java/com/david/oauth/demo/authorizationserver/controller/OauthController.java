package com.david.oauth.demo.authorizationserver.controller;

import com.david.oauth.demo.authorizationserver.enums.ResponseTypeEnum;
import com.david.oauth.demo.authorizationserver.service.AuthorizationService;
import com.david.oauth.demo.authorizationserver.service.ClientManagement;
import com.david.oauth.demo.oauthcommons.entity.Client;
import com.david.oauth.demo.oauthcommons.entity.ResponseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
public class OauthController {

    private ClientManagement clientService;

    private AuthorizationService authorizationService;

    @Autowired
    public OauthController(ClientManagement clientService, AuthorizationService authorizationService) {
        this.clientService = clientService;
        this.authorizationService = authorizationService;
    }

    @GetMapping("health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }

    @GetMapping("authorize")
    public RedirectView getAuthorizationCode(@RequestParam("response_type") String responseType,
                                             @RequestParam("state") String state,
                                             @RequestParam("client_id") String clientId,
                                             @RequestParam("redirect_uri") String redirectUri) {

        RedirectView redirect = new RedirectView(redirectUri);
        try {
            Client client = this.clientService.validateOauthClient(clientId, redirectUri, responseType);
            Map<String, String> attributesMap = new HashMap<>();
            attributesMap.put(ResponseTypeEnum.CODE.getType(), this.authorizationService.generateAuthorizationCodeForClient(client, state));
            attributesMap.put("state", state);
            redirect.setStatusCode(HttpStatus.FOUND);
            redirect.setAttributesMap(attributesMap);
        } catch (Exception e) {
            redirect.setStatusCode(HttpStatus.BAD_REQUEST);
        }
        return redirect;
    }

    @PostMapping(path = "token")
    public ResponseEntity<?> getToken(HttpServletRequest request) {
        try {
            Client client = this.clientService.validateOauthClient(request);
            ResponseToken responseToken = this.authorizationService.createResponseAccessToken(client);
            return new ResponseEntity<>(responseToken, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
        }
    }
}
