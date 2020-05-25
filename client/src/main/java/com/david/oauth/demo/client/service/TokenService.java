package com.david.oauth.demo.client.service;

import com.david.oauth.demo.oauthcommons.util.JwtTokenUtil;
import com.david.oauth.demo.oauthcommons.util.OauthKeyStoreUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;

import static com.david.oauth.demo.oauthcommons.constants.Constants.KEY_STORE_ALIAS_STATE;

@Service
public class TokenService {

    @Value("${oauth.key-store.password}")
    public String keyStorePassword;

    @Value("${oauth.key-store.name}")
    public String keyStoreName;

    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public TokenService(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public String createAndSaveRequestState() throws IOException, KeyStoreException {
        String state = jwtTokenUtil.generateState();
        this.saveValueIntoKeyStore(KEY_STORE_ALIAS_STATE, state);
        return state;
    }

    public String getValueFromKeyStore(String value) throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException, IOException {
        OauthKeyStoreUtil keyStoreUtil = new OauthKeyStoreUtil(this.keyStoreName, this.keyStorePassword);
        return keyStoreUtil.getValueFromKeyStore(value);
    }

    public void saveValueIntoKeyStore(String alias, String value) throws KeyStoreException, IOException {
        OauthKeyStoreUtil keyStoreUtil = new OauthKeyStoreUtil(this.keyStoreName, this.keyStorePassword);
        keyStoreUtil.saveEntry(alias, value);
    }

}
