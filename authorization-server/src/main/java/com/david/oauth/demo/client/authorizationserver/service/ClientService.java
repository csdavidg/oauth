package com.david.oauth.demo.client.authorizationserver.service;

import com.david.oauth.demo.client.authorizationserver.dao.ClientDAO;
import com.david.oauth.demo.client.authorizationserver.entity.Client;
import com.david.oauth.demo.client.authorizationserver.enums.ResponseTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<Client> findByClientId(String clientId) {
        return clientDAO.findByClientId(clientId);
    }

    public Client validateOauthClient(String clientId, String redirectUri, String responseType) throws IllegalArgumentException {
        try {
            if (!ResponseTypeEnum.CODE.equals(ResponseTypeEnum.valueOf(responseType))) {
                throw new IllegalArgumentException();
            }
            Client client = this.findByClientId(clientId).orElseThrow(IllegalArgumentException::new);
            if (!client.getRedirectUri().equals(redirectUri)) {
                throw new IllegalArgumentException();
            }
            return client;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid client");
        }
    }
}
