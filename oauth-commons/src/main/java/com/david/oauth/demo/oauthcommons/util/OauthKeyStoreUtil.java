package com.david.oauth.demo.oauthcommons.util;

import org.apache.tomcat.util.security.Escape;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import static com.david.oauth.demo.oauthcommons.constants.Constants.KEY_STORE_ALGORITHM;
import static com.david.oauth.demo.oauthcommons.constants.Constants.KEY_STORE_TYPE;

public class OauthKeyStoreUtil {

    private final String keyStoreName;
    private final String keyStorePassword;

    private KeyStore keyStore;
    private KeyStore.ProtectionParameter protectionParameter;

    public OauthKeyStoreUtil(String keyStoreName, String keyStorePassword) {
        this.keyStoreName = keyStoreName;
        this.keyStorePassword = keyStorePassword;
        this.initializeKeyStore();
    }

    private void initializeKeyStore() {
        FileInputStream fis = null;
        try {
            this.keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
            fis = this.validateExistentKeyStoreFile();
            this.keyStore.load(fis, this.keyStorePassword.toCharArray());
            this.protectionParameter = new KeyStore.PasswordProtection(keyStorePassword.toCharArray());
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private FileInputStream validateExistentKeyStoreFile() {
        try {
            return new FileInputStream(this.keyStoreName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateAndCloseKeyStore() {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(this.keyStoreName);
            this.keyStore.store(fos, this.keyStorePassword.toCharArray());
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveEntry(String alias, String entry) {
        try {
            SecretKey mySecretKey = new SecretKeySpec(entry.getBytes(), KEY_STORE_ALGORITHM);
            KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(mySecretKey);
            this.keyStore.setEntry(alias, skEntry, this.protectionParameter);
            updateAndCloseKeyStore();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public String getValueFromKeyStore(String alias) {
        String value = null;
        try {
            KeyStore.Entry pkEntry = this.keyStore.getEntry(alias, this.protectionParameter);
            if (pkEntry != null) {
                KeyStore.SecretKeyEntry keyEntry = (KeyStore.SecretKeyEntry) pkEntry;
                SecretKeySpec secSpec = (SecretKeySpec) (keyEntry).getSecretKey();
                value = new String(secSpec.getEncoded());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public void deleteValueInKeyStore(String key) {
        try {
            this.keyStore.deleteEntry(key);
            updateAndCloseKeyStore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
