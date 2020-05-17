package com.david.oauth.demo.authorizationserver.controller;

import com.david.oauth.demo.authorizationserver.service.ClientManagement;
import com.david.oauth.demo.oauthcommons.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("client")
public class ClientController {

    private ClientManagement clientService;

    @Autowired
    public ClientController(ClientManagement clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> getAllClients() {
        return clientService.findAll();
    }


}
