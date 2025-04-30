package com.fine_kuo.by_shardingsphere_jdbc.config;


import org.apache.shardingsphere.encrypt.api.context.EncryptContext;
import org.apache.shardingsphere.encrypt.spi.EncryptAlgorithm;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;

public final class CustomAesEncryptor implements EncryptAlgorithm {

    private static final String AES_KEY = "aes-key-value";
    private Properties props = new Properties();

    @Override
    public void init(Properties props) {
        this.props = props;
    }

    @Override
    public String encrypt(Object plaintext, EncryptContext encryptContext) {
        if (null == plaintext) {
            return null;
        }

        try {
            // Generate a random IV
            byte[] iv = generateRandomIV();
            String ivBase64 = Base64.getEncoder().encodeToString(iv);

            // Get the AES key
            byte[] aesKey = getAesKey();

            // Encrypt the plaintext
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            byte[] encrypted = cipher.doFinal(plaintext.toString().getBytes(StandardCharsets.UTF_8));
            String encryptedBase64 = Base64.getEncoder().encodeToString(encrypted);

            // Return IV + encrypted data in format {iv}{encryptedData}
            return ivBase64 + encryptedBase64;
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    public String decrypt(Object cipherObject, EncryptContext encryptContext) {
        if (null == cipherObject) {
            return null;
        }

        String ciphertext = cipherObject.toString();
        try {

            // The first 24 characters (16 bytes in Base64) are the IV
            String ivBase64 = ciphertext.substring(0, 24);
            String encryptedBase64 = ciphertext.substring(24);

            byte[] iv = Base64.getDecoder().decode(ivBase64);
            byte[] encryptedData = Base64.getDecoder().decode(encryptedBase64);

            // Get the AES key
            byte[] aesKey = getAesKey();

            // Decrypt the data
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] decrypted = cipher.doFinal(encryptedData);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }

    private byte[] generateRandomIV() {
        byte[] iv = new byte[16]; // AES block size is 16 bytes
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    private byte[] getAesKey() throws NoSuchAlgorithmException {
        String aesKeyValue = props.getProperty(AES_KEY);
        if (aesKeyValue == null || aesKeyValue.isEmpty()) {
            throw new RuntimeException("AES key is not configured");
        }

        // Generate a 256-bit key from the provided key value
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = new SecureRandom(aesKeyValue.getBytes(StandardCharsets.UTF_8));
        keyGenerator.init(256, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    @Override
    public String getType() {
        return "AES_CUSTOM";
    }

}
