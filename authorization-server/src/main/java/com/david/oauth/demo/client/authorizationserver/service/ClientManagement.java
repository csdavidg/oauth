package com.david.oauth.demo.client.authorizationserver.service;

import com.david.oauth.demo.client.authorizationserver.entity.Client;

import java.util.List;

public interface ClientManagement {

    List<Client> findAll();

    Client save(Client employee);

    Client findById(Long id);

    void deleteById(Long id);

    Client findByClientId(String clientId);

}
