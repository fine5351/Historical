package com.finekuo.normalcore.util;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

public class DynamicIVAesEncryptUtil {

    private static final String AES_CBC_PKCS5 = "AES/CBC/PKCS5Padding";

    private static  byte[] generateIV() {
        byte[] iv = new byte[16]; // AES block size is 16 bytes
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    @SneakyThrows
    public static String encrypt(Object o, String key) {
        if (o == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5);
        byte[] iv = generateIV();
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(key), ivSpec);
        byte[] encryptedBytes = cipher.doFinal(o.toString().getBytes(StandardCharsets.UTF_8));
        byte[] combined = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);
        return Base64.getEncoder().encodeToString(combined);
    }

    @SneakyThrows
    public static String decrypt(Object o, String secretkey) {
        if (o == null) {
            return null;
        }
        byte[] combined = Base64.getDecoder().decode(o.toString());
        byte[] iv = new byte[16]; // IV is always 16 bytes for AES
        byte[] encryptedBytes = new byte[combined.length - 16];
        System.arraycopy(combined, 0, iv, 0, 16);
        System.arraycopy(combined, 16, encryptedBytes, 0, encryptedBytes.length);

        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(secretkey), ivSpec);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    private static SecretKeySpec getSecretKeySpec(String key) {
        if (key == null || key.length() != 16) {
            throw new IllegalArgumentException("Key must be 16 characters long");
        }
        return new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
    }

}
