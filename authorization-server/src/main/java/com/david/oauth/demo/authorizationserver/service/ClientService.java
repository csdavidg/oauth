package com.david.oauth.demo.authorizationserver.service;

import com.david.oauth.demo.authorizationserver.dao.ClientDAO;
import com.david.oauth.demo.authorizationserver.enums.GrantTypeEnum;
import com.david.oauth.demo.authorizationserver.enums.ResponseTypeEnum;
import com.david.oauth.demo.oauthcommons.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Service
public class ClientService implements ClientManagement {

    private ClientDAO clientDAO;

    @Autowired
    public ClientService(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    @Override
    public List<Client> findAll() {
        return clientDAO.findAll();
    }

    @Override
    public Client save(Client client) {
        return clientDAO.save(client);
    }

    @Override
    public Client findById(Long id) {
        return clientDAO.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        clientDAO.deleteById(id);
    }

    public Client validateOauthClient(String clientId, String redirectUri, String responseType) throws IllegalArgumentException {
        try {
            if (!ResponseTypeEnum.CODE.equals(ResponseTypeEnum.getValueFromString(responseType))) {
                throw new IllegalArgumentException();
            }
            Client client = this.clientDAO.findByClientId(clientId).orElseThrow(IllegalArgumentException::new);
            if (!client.getRedirectUri().equals(redirectUri)) {
                throw new IllegalArgumentException();
            }
            return client;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid client");
        }
    }

    public Client validateOauthClient(HttpServletRequest request) throws IllegalArgumentException {
        try {

            if (GrantTypeEnum.getValueFromString(request.getParameter("grant_type")) == null) {
                throw new IllegalArgumentException();
            }

            HttpHeaders headers = Collections
                    .list(request.getHeaderNames())
                    .stream()
                    .collect(toMap(Function.identity(), h -> Collections.list(request.getHeaders(h)),
                            (oldValue, newValue) -> newValue, HttpHeaders::new));

            String credentials = headers.getFirst(HttpHeaders.AUTHORIZATION).replace("Basic", "").trim();
            String[] credentialsDecoded = new String(Base64.getDecoder().decode(credentials)).split(":");
            return this.clientDAO.findByClientIdAndClientSecretAndAuthorizationCode(credentialsDecoded[0], credentialsDecoded[1], request.getParameter("code"))
                    .orElseThrow(IllegalArgumentException::new);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid client");
        }
    }
}
