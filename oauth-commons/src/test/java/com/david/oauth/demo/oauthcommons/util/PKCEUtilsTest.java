package com.david.oauth.demo.oauthcommons.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PKCEUtilsTest {


    private String ALGORITHM = "SHA-256";

    @Test
    public void validateCodeChallengeCreated(){
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil("TEST");
        String codeVerifier = jwtTokenUtil.generateRandomString();
        PKCEUtil pkceUtil = new PKCEUtil(ALGORITHM);
        String hashCreated = pkceUtil.getCodeChallenge(codeVerifier);
        assertNotNull(hashCreated);
    }

}
