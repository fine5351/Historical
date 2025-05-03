package com.finekuo.encryptor.aes;

import com.finekuo.normalcore.util.DynamicIVAesEncryptUtil;
import org.apache.shardingsphere.encrypt.spi.EncryptAlgorithm;
import org.apache.shardingsphere.encrypt.spi.EncryptAlgorithmMetaData;
import org.apache.shardingsphere.infra.algorithm.core.config.AlgorithmConfiguration;
import org.apache.shardingsphere.infra.algorithm.core.context.AlgorithmSQLContext;

import java.util.Properties;

public class CustomAesEncryptor implements EncryptAlgorithm {

    private String secretKey;
    private Properties properties;

    @Override
    public void init(Properties props) {
        this.properties = props;
        String aesKeyValue = props.getProperty("aes-key-value");
        if (aesKeyValue == null || aesKeyValue.length() != 16) {
            throw new IllegalArgumentException("Invalid AES key. Key must be 16 characters long.");
        }
        secretKey = aesKeyValue;
    }


    @Override
    public String getType() {
        return "CUSTOM_AES";
    }

    @Override
    public Object encrypt(Object o, AlgorithmSQLContext algorithmSQLContext) {
        if (o == null) {
            return null;
        }
        return DynamicIVAesEncryptUtil.encrypt(o.toString(), secretKey);
    }

    @Override
    public Object decrypt(Object o, AlgorithmSQLContext algorithmSQLContext) {
        if (o == null) {
            return null;
        }
        try {
            return DynamicIVAesEncryptUtil.decrypt(o.toString(), secretKey);
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
