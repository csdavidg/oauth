package com.david.oauth.demo.oauthcommons.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OauthKeyStoreUtilTest {

    public static final String KEY_STORE_NAME = "name";
    public static final String KEY_STORE_PASSWORD = "password";
    public static final String KEY_STORE_ALIAS = "alias";

    @Test
    public void saveValueIntoKeyStore() {
        OauthKeyStoreUtil oauthKeyStoreUtil = new OauthKeyStoreUtil(KEY_STORE_NAME, KEY_STORE_PASSWORD);
        JwtTokenUtil jwt = new JwtTokenUtil("HOLA");
        String state = jwt.generateState();
        oauthKeyStoreUtil.saveEntry(KEY_STORE_ALIAS, state);
        String value = oauthKeyStoreUtil.getValueFromKeyStore(KEY_STORE_ALIAS);
        assertEquals(value, state);
    }

    @Test
    public void validateKeyDeletedSuccessful() {
        OauthKeyStoreUtil oauthKeyStoreUtil = new OauthKeyStoreUtil(KEY_STORE_NAME, KEY_STORE_PASSWORD);
        oauthKeyStoreUtil.saveEntry(KEY_STORE_ALIAS, "VALUE_TO_SAVE");

        oauthKeyStoreUtil = new OauthKeyStoreUtil(KEY_STORE_NAME, KEY_STORE_PASSWORD);
        oauthKeyStoreUtil.deleteValueInKeyStore(KEY_STORE_ALIAS);

        oauthKeyStoreUtil = new OauthKeyStoreUtil(KEY_STORE_NAME, KEY_STORE_PASSWORD);
        String value = oauthKeyStoreUtil.getValueFromKeyStore(KEY_STORE_ALIAS);
        assertNull(value);
    }
}
