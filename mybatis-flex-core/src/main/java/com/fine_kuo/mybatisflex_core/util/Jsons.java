package com.fine_kuo.mybatisflex_core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class Jsons {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    private Jsons() {

    }

    @SneakyThrows
    public static String toJson(Object object) {
        return OBJECT_MAPPER.writeValueAsString(object);
    }

    @SneakyThrows
    public static <T> T fromJson(String json, Class<T> clazz) {
        return OBJECT_MAPPER.readValue(json, clazz);
    }

}
