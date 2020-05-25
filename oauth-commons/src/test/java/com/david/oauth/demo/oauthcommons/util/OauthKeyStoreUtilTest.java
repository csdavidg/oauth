package com.david.oauth.demo.oauthcommons.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OauthKeyStoreUtilTest {

    public static final String KEY_STORE_NAME = "name";
    public static final String KEY_STORE_PASSWORD = "password";
    public static final String KEY_STORE_ALIAS = "alias";

    @Test
    public void saveValueIntoKeyStore() throws IOException, KeyStoreException, UnrecoverableEntryException, NoSuchAlgorithmException {
        OauthKeyStoreUtil oauthKeyStoreUtil = new OauthKeyStoreUtil(KEY_STORE_NAME, KEY_STORE_PASSWORD);
        JwtTokenUtil jwt = new JwtTokenUtil("HOLA");
        String state = jwt.generateState();
        oauthKeyStoreUtil.saveEntry(KEY_STORE_ALIAS, state);
        String value = oauthKeyStoreUtil.getValueFromKeyStore(KEY_STORE_ALIAS);
        assertEquals(value, state);
    }
}
