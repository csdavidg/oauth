package com.david.oauth.demo.authorizationserver.service;


import com.david.oauth.demo.oauthcommons.entity.Client;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ClientManagement {

    List<Client> findAll();

    Client save(Client employee);

    Client findById(Long id);

    void deleteById(Long id);

    Client validateOauthClient(String clientId, String redirectUri, String responseType) throws IllegalArgumentException;

    Client validateOauthClient(HttpServletRequest request) throws IllegalArgumentException;

}
