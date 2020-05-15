package com.david.oauth.demo.client.authorizationserver.controller;

import com.david.oauth.demo.client.authorizationserver.component.JwtTokenGenerator;
import com.david.oauth.demo.client.authorizationserver.config.ClientConfig;
import com.david.oauth.demo.client.authorizationserver.entity.Client;
import com.david.oauth.demo.client.authorizationserver.service.ClientManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/oauth")
public class OauthController {

    @Resource
    private ClientConfig clientConfig;

    private ClientManagement clientService;

    private JwtTokenGenerator jwtTokenGenerator;

    @Autowired
    public OauthController(ClientManagement clientService, JwtTokenGenerator jwtTokenGenerator) {
        this.clientService = clientService;
        this.jwtTokenGenerator = jwtTokenGenerator;
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
        Client client = clientService.findByClientId(clientId);

        if (Objects.nonNull(client)) {

            String authorizationCode = jwtTokenGenerator.generateAuthorizationCode();
            client.setAuthorizationCode(authorizationCode);
            clientService.save(client);

            Map<String, String> attributesMap = new HashMap<>();
            attributesMap.put("code", authorizationCode);

            redirect.setStatusCode(HttpStatus.FOUND);
            redirect.setAttributesMap(attributesMap);
        } else {
            redirect.setStatusCode(HttpStatus.BAD_REQUEST);
        }
        return redirect;
    }

    @PostMapping("token")
    public ResponseEntity<String> getToken() {
        return null;
    }
}
