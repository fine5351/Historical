package com.fine_kuo.encryptor.aes;

import org.apache.shardingsphere.encrypt.spi.EncryptAlgorithm;
import org.apache.shardingsphere.encrypt.spi.EncryptAlgorithmMetaData;
import org.apache.shardingsphere.infra.algorithm.core.config.AlgorithmConfiguration;
import org.apache.shardingsphere.infra.algorithm.core.context.AlgorithmSQLContext;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;

public class CustomAesEncryptor implements EncryptAlgorithm {

    private static final String AES_CBC_PKCS5 = "AES/CBC/PKCS5Padding";
    private SecretKeySpec secretKey;
    private Properties properties;

    @Override
    public void init(Properties props) {
        this.properties = props;
        String key = props.getProperty("aes-key-value");
        if (key == null || key.length() != 16) {
            throw new IllegalArgumentException("Invalid AES key. Key must be 16 characters long.");
        }
        secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
    }


    @Override
    public String getType() {
        return "CUSTOM_AES";
    }

    private byte[] generateIV() {
        byte[] iv = new byte[16]; // AES block size is 16 bytes
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    @Override
    public Object encrypt(Object o, AlgorithmSQLContext algorithmSQLContext) {
        if (o == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5);
            byte[] iv = generateIV();
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] encryptedBytes = cipher.doFinal(o.toString().getBytes(StandardCharsets.UTF_8));
            byte[] combined = new byte[iv.length + encryptedBytes.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);
            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    @Override
    public Object decrypt(Object o, AlgorithmSQLContext algorithmSQLContext) {
        if (o == null) {
            return null;
        }
        try {
            byte[] combined = Base64.getDecoder().decode(o.toString());
            byte[] iv = new byte[16]; // IV is always 16 bytes for AES
            byte[] encryptedBytes = new byte[combined.length - 16];
            System.arraycopy(combined, 0, iv, 0, 16);
            System.arraycopy(combined, 16, encryptedBytes, 0, encryptedBytes.length);

            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }

    @Override
    public EncryptAlgorithmMetaData getMetaData() {
        return new EncryptAlgorithmMetaData(true, false, false);
    }

    @Override
    public AlgorithmConfiguration toConfiguration() {
        return new AlgorithmConfiguration(getType(), properties);
    }

}
