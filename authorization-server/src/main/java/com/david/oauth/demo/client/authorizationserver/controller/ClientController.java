package com.david.oauth.demo.client.authorizationserver.controller;

import com.david.oauth.demo.client.authorizationserver.entity.Client;
import com.david.oauth.demo.client.authorizationserver.service.ClientManagement;
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
    public ClientController(ClientManagement clientService){
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> getAllClients(){
        return clientService.findAll();
    }


}
