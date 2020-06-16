package com.david.oauth.demo.authorizationserver.service;

import com.david.oauth.demo.authorizationserver.dao.ClientDAO;
import com.david.oauth.demo.oauthcommons.entity.Client;
import com.david.oauth.demo.oauthcommons.enums.GrantTypeEnum;
import com.david.oauth.demo.oauthcommons.enums.ResponseTypeEnum;
import com.david.oauth.demo.oauthcommons.managers.KeyStoreManager;
import com.david.oauth.demo.oauthcommons.util.PKCEUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static com.david.oauth.demo.oauthcommons.constants.Constants.ALGORITHM;
import static com.david.oauth.demo.oauthcommons.constants.Constants.CODE_CHALLENGE;
import static com.david.oauth.demo.oauthcommons.constants.Constants.CODE_VERIFIER;
import static com.david.oauth.demo.oauthcommons.constants.Constants.GRANT_TYPE;
import static java.util.stream.Collectors.toMap;

@Service
public class ClientService implements ClientManagement {

    private final ClientDAO clientDAO;
    private final KeyStoreManager keyStoreManager;

    @Autowired
    public ClientService(ClientDAO clientDAO, KeyStoreManager keyStoreManager) {
        this.clientDAO = clientDAO;
        this.keyStoreManager = keyStoreManager;
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
            ResponseTypeEnum responseTypeEnum = ResponseTypeEnum.getValueFromString(responseType);
            if (responseTypeEnum == null) {
                throw new IllegalArgumentException();
            }
            Client client = clientDAO.findByClientId(clientId).orElseThrow(IllegalArgumentException::new);
            if (!client.getRedirectUri().equals(redirectUri)) {
                throw new IllegalArgumentException();
            }
            return client;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid client");
        }
    }

    @Override
    public Client validateOauthClient(HttpServletRequest request) throws IllegalArgumentException {
        try {

            GrantTypeEnum grantType = GrantTypeEnum.getValueFromString(request.getParameter(GRANT_TYPE));
            if (grantType == null) {
                throw new IllegalArgumentException();
            }

            HttpHeaders headers = Collections
                    .list(request.getHeaderNames())
                    .stream()
                    .collect(toMap(Function.identity(), h -> Collections.list(request.getHeaders(h)),
                            (oldValue, newValue) -> newValue, HttpHeaders::new));

            String credentials = headers.getFirst(HttpHeaders.AUTHORIZATION).replace("Basic", "").trim();
            String[] credentialsDecoded = new String(Base64.getDecoder().decode(credentials)).split(":");
            Client client = clientDAO.findByClientIdAndClientSecret(credentialsDecoded[0], credentialsDecoded[1])
                    .orElseThrow(IllegalArgumentException::new);

            if (grantType.equals(GrantTypeEnum.AUTHORIZATION_CODE)) {
                recomputeAndValidateCodeChallenge(request.getParameter(CODE_VERIFIER));
                if (request.getParameter("code").equals(client.getAuthorizationCode())
                        && request.getParameter("redirect_uri").equals(client.getRedirectUri())) {

                    client.setAuthorizationCode(null);
                    client.setState(null);
                    clientDAO.save(client);
                } else {
                    throw new IllegalArgumentException();
                }
            }
            return client;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid client");
        }
    }

    @Override
    public void saveCodeChallengeAndAlgorithm(String codeChallenge, String algorithm) {
        keyStoreManager.saveValueIntoKeyStore(CODE_CHALLENGE, new String(Base64.getDecoder().decode(codeChallenge.getBytes())));
        keyStoreManager.saveValueIntoKeyStore(ALGORITHM, algorithm);
    }

    @Override
    public void recomputeAndValidateCodeChallenge(String codeVerifier) {
        try {
            String algorithm = keyStoreManager.getValueFromKeyStore(ALGORITHM);
            String codeChallenge = keyStoreManager.getValueFromKeyStore(CODE_CHALLENGE);
            PKCEUtil pkceUtil = new PKCEUtil(algorithm);
            if (!pkceUtil.getCodeChallenge(codeVerifier).equals(codeChallenge)) {
                throw new IllegalArgumentException("Invalid code challenge");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
