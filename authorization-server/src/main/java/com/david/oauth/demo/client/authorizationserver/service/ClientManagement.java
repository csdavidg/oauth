package com.david.oauth.demo.client.authorizationserver.service;

import com.david.oauth.demo.client.authorizationserver.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientManagement {

    List<Client> findAll();

    Client save(Client employee);

    Client findById(Long id);

    void deleteById(Long id);

    Optional<Client> findByClientId(String clientId);

    public Client validateOauthClient(String clientId, String redirectUri, String responseType) throws IllegalArgumentException;

}
