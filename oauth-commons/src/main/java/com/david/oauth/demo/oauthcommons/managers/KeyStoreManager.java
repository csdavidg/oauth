package com.david.oauth.demo.oauthcommons.managers;

import com.david.oauth.demo.oauthcommons.util.JwtTokenUtil;
import com.david.oauth.demo.oauthcommons.util.OauthKeyStoreUtil;

public class KeyStoreManager {

    public final String keyStorePassword;
    public final String keyStoreName;
    private final JwtTokenUtil jwtTokenUtil;

    public KeyStoreManager(JwtTokenUtil jwtTokenUtil, String keyStoreName, String keyStorePassword) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.keyStoreName = keyStoreName;
        this.keyStorePassword = keyStorePassword;
    }

    public String getValueFromKeyStore(String value) {
        OauthKeyStoreUtil keyStoreUtil = new OauthKeyStoreUtil(keyStoreName, keyStorePassword);
        return keyStoreUtil.getValueFromKeyStore(value);
    }

    public void saveValueIntoKeyStore(String alias, String value) {
        OauthKeyStoreUtil keyStoreUtil = new OauthKeyStoreUtil(keyStoreName, keyStorePassword);
        keyStoreUtil.saveEntry(alias, value);
    }

    public void deleteValueInKeyStore(String alias) {
        OauthKeyStoreUtil keyStoreUtil = new OauthKeyStoreUtil(keyStoreName, keyStorePassword);
        keyStoreUtil.deleteValueInKeyStore(alias);
    }
}
