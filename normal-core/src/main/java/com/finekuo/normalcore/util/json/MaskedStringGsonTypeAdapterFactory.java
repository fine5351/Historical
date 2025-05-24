package com.finekuo.normalcore.util.json;

import com.finekuo.normalcore.annotation.MaskedString;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonNull;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MaskedStringGsonTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        boolean hasMaskedStringField = false;
        for (Field field : rawType.getDeclaredFields()) {
            if (field.getType() == String.class && field.isAnnotationPresent(MaskedString.class)) {
                hasMaskedStringField = true;
                break;
            }
        }

        if (!hasMaskedStringField) {
            return null; // Gson will use its default adapter
        }
        return new MaskedTypeAdapter<>(gson, type);
    }

    private static class MaskedTypeAdapter<T> extends TypeAdapter<T> {
        private final Gson gson;
        private final TypeToken<T> type;

        MaskedTypeAdapter(Gson gson, TypeToken<T> type) {
            this.gson = gson;
            this.type = type;
        }

        @Override
        public void write(JsonWriter out, T value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            // Get the default adapter for JsonObject to write the final result
            TypeAdapter<JsonObject> jsonObjectAdapter = gson.getAdapter(JsonObject.class);
            // Get the default adapter for the type T to convert it to JsonObject
            TypeAdapter<T> delegateAdapter = gson.getDelegateAdapter(MaskedStringGsonTypeAdapterFactory.this, type);
            JsonObject jsonObject = delegateAdapter.toJsonTree(value).getAsJsonObject();

            Class<?> rawType = value.getClass();
            for (Field field : rawType.getDeclaredFields()) {
                if (field.getType() == String.class && field.isAnnotationPresent(MaskedString.class)) {
                    field.setAccessible(true); // Make field accessible
                    try {
                        String originalValue = (String) field.get(value);
                        if (originalValue != null) {
                            String maskedValue = IntStream.range(0, originalValue.length())
                                    .mapToObj(i -> "*")
                                    .collect(Collectors.joining());
                            jsonObject.addProperty(field.getName(), maskedValue);
                        } else {
                            jsonObject.add(field.getName(), JsonNull.INSTANCE);
                        }
                    } catch (IllegalAccessException e) {
                        // Log or handle exception if necessary
                        // For now, we'll let the original value (or lack thereof) in jsonObject persist
                    }
                }
            }
            jsonObjectAdapter.write(out, jsonObject);
        }

        @Override
        public T read(JsonReader in) throws IOException {
            // Delegate to Gson's default adapter for deserialization
            return gson.getDelegateAdapter(MaskedStringGsonTypeAdapterFactory.this, type).read(in);
        }
    }
}
