package com.finekuo.normalcore.util;

import com.finekuo.normalcore.util.json.MaskedStringGsonTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
public final class Gsons {

    private static final Gson GSON_INSTANCE = createGson();

    private Gsons() {
        // Prevent instantiation
    }

    private static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(new MaskedStringGsonTypeAdapterFactory())
                .registerTypeAdapter(LocalDateTime.class, localDateTimeAdapter())
                .registerTypeAdapter(LocalDate.class, localDateAdapter())
                .registerTypeAdapter(LocalTime.class, localTimeAdapter())
                .serializeNulls()
                .create();
    }

    private static TypeAdapter<LocalDateTime> localDateTimeAdapter() {
        return new TypeAdapter<LocalDateTime>() {

            @Override
            public void write(JsonWriter out, LocalDateTime value) throws IOException {
                out.value(value == null ? null : value.toString());
            }

            @Override
            public LocalDateTime read(JsonReader in) throws IOException {
                String str = in.nextString();
                return str == null ? null : LocalDateTime.parse(str);
            }
        };
    }

    private static TypeAdapter<LocalDate> localDateAdapter() {
        return new TypeAdapter<LocalDate>() {

            @Override
            public void write(JsonWriter out, LocalDate value) throws IOException {
                out.value(value == null ? null : value.toString());
            }

            @Override
            public LocalDate read(JsonReader in) throws IOException {
                String str = in.nextString();
                return str == null ? null : LocalDate.parse(str);
            }
        };
    }

    private static TypeAdapter<LocalTime> localTimeAdapter() {
        return new TypeAdapter<LocalTime>() {

            @Override
            public void write(JsonWriter out, LocalTime value) throws IOException {
                out.value(value == null ? null : value.toString());
            }

            @Override
            public LocalTime read(JsonReader in) throws IOException {
                String str = in.nextString();
                return str == null ? null : LocalTime.parse(str);
            }
        };
    }

    public static String toJson(Object object) {
        return GSON_INSTANCE.toJson(object);
    }

    public static JsonObject toJsonTree(Object object) {
        return GSON_INSTANCE.toJsonTree(object).getAsJsonObject();
    }

    public static <T> T fromJson(JsonObject masked, Class<T> clazz) {
        return GSON_INSTANCE.fromJson(masked, clazz);
    }

    /**
     * 回傳物件結構 fingerprint，格式為 json，key 對應的 value 轉為 type 字串
     */
    public static String getFingerprint(Object object) {
        JsonObject jsonObject = toJsonTree(object);
        JsonObject typeObject = buildTypeJson(jsonObject);
        return GSON_INSTANCE.toJson(typeObject);
    }

    public static String getMaskSettingsTemplate(Object object) {
        JsonObject jsonObject = toJsonTree(object);
        JsonObject typeObject = buildTypeJson(jsonObject);
        return GSON_INSTANCE.toJson(typeObject);
    }

    /**
     * 遞迴將 jsonObject 轉為 type json
     */
    private static JsonObject buildTypeJson(JsonObject jsonObject) {
        JsonObject result = new JsonObject();
        for (String key : jsonObject.keySet()) {
            var value = jsonObject.get(key);
            if (value.isJsonObject()) {
                result.add(key, buildTypeJson(value.getAsJsonObject()));
            } else if (value.isJsonArray()) {
                result.addProperty(key, "array");
            } else if (value.isJsonNull()) {
                // 不輸出 null 欄位
            } else if (value.isJsonPrimitive()) {
                if (value.getAsJsonPrimitive().isString()) {
                    result.addProperty(key, "string");
                } else if (value.getAsJsonPrimitive().isNumber()) {
                    result.addProperty(key, "number");
                } else if (value.getAsJsonPrimitive().isBoolean()) {
                    result.addProperty(key, "boolean");
                } else {
                    result.addProperty(key, "primitive");
                }
            }
        }
        return result;
    }

}