package com.david.oauth.demo.client.authorizationserver.controller;

import com.david.oauth.demo.client.authorizationserver.entity.Client;
import com.david.oauth.demo.client.authorizationserver.enums.ResponseTypeEnum;
import com.david.oauth.demo.client.authorizationserver.service.AuthorizationService;
import com.david.oauth.demo.client.authorizationserver.service.ClientManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

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
            Client client = clientService.validateOauthClient(clientId, redirectUri, responseType);
            Map<String, String> attributesMap = new HashMap<>();
            attributesMap.put(ResponseTypeEnum.CODE.getType(), authorizationService.generateAuthorizationCodeForClient(client, state));
            redirect.setStatusCode(HttpStatus.FOUND);
            redirect.setAttributesMap(attributesMap);
        } catch (Exception e) {
            redirect.setStatusCode(HttpStatus.BAD_REQUEST);
        }
        return redirect;
    }

    @PostMapping("token")
    public ResponseEntity<String> getToken(HttpRequest request) {
        request.getHeaders();
        return null;
    }
}
