package com.finekuo.springdatajpa.convert;

import com.finekuo.normalcore.util.DynamicIVAesEncryptUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;

@Converter
public class RocIdConvert implements AttributeConverter<String, String> {

    @Value(value = "${encrypt.employee.roc_id.aes-key-value}")
    private String aesKeyValue;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return attribute == null ? null : DynamicIVAesEncryptUtil.encrypt(attribute, aesKeyValue);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData == null ? null : DynamicIVAesEncryptUtil.decrypt(dbData, aesKeyValue);
    }

}
