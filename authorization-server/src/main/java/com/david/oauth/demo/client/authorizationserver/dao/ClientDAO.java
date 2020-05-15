package com.david.oauth.demo.client.authorizationserver.dao;

import com.david.oauth.demo.client.authorizationserver.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientDAO extends JpaRepository<Client, Long> {

    Optional<Client> findByClientId(String clientId);

}
