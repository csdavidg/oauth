package com.david.oauth.demo.client.authorizationserver.service;

import com.david.oauth.demo.client.authorizationserver.dao.ClientDAO;
import com.david.oauth.demo.client.authorizationserver.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Client findByClientId(String clientId) {
        return clientDAO.findByClientId(clientId).orElse(null);
    }

}
