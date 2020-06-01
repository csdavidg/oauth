package com.david.oauth.demo.oauthcommons.managers;

import com.david.oauth.demo.oauthcommons.util.JwtTokenUtil;
import com.david.oauth.demo.oauthcommons.util.OauthKeyStoreUtil;

import static com.david.oauth.demo.oauthcommons.constants.Constants.KEY_STORE_ALIAS_STATE;

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
        OauthKeyStoreUtil keyStoreUtil = new OauthKeyStoreUtil(this.keyStoreName, this.keyStorePassword);
        return keyStoreUtil.getValueFromKeyStore(value);
    }

    public void saveValueIntoKeyStore(String alias, String value) {
        OauthKeyStoreUtil keyStoreUtil = new OauthKeyStoreUtil(this.keyStoreName, this.keyStorePassword);
        keyStoreUtil.saveEntry(alias, value);
    }
}
