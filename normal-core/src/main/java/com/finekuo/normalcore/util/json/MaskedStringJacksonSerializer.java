package com.finekuo.normalcore.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MaskedStringJacksonSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            String maskedValue = IntStream.range(0, value.length())
                    .mapToObj(i -> "*")
                    .collect(Collectors.joining());
            gen.writeString(maskedValue);
        }
    }
}
