package com.david.oauth.demo.authorizationserver.dao;

import com.david.oauth.demo.oauthcommons.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientDAO extends JpaRepository<Client, Long> {

    Optional<Client> findByClientId(String clientId);

    Optional<Client> findByClientIdAndClientSecretAndAuthorizationCode(String clientId, String clientSecret, String code);

    Optional<Client> findByClientIdAndClientSecret(String clientId, String clientSecret);

    Optional<Client> findByClientIdAndClientSecretAndRedirectUri(String clientId, String clientSecret, String redirectUri);

}
