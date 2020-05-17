package com.david.oauth.demo.client.authorizationserver.service;

import com.david.oauth.demo.client.authorizationserver.entity.Client;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface ClientManagement {

    List<Client> findAll();

    Client save(Client employee);

    Client findById(Long id);

    void deleteById(Long id);

    Client validateOauthClient(String clientId, String redirectUri, String responseType) throws IllegalArgumentException;

    Client validateOauthClient(HttpServletRequest request) throws IllegalArgumentException;

}
