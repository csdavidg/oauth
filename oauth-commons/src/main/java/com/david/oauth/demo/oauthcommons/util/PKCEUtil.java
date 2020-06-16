package com.david.oauth.demo.oauthcommons.util;

import lombok.AllArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@AllArgsConstructor
public class PKCEUtil {

    private final String ALGORITHM;

    public String getCodeChallenge(String codeVerifier) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NullPointerException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error trying hash the code verifier");
        }
    }

}
