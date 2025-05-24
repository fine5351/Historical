package com.finekuo.normalcore.util;

import com.finekuo.normalcore.util.json.MaskedStringGsonTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class Gsons {

    private static final Gson GSON_INSTANCE = new GsonBuilder()
            .registerTypeAdapterFactory(new MaskedStringGsonTypeAdapterFactory())
            .serializeNulls()
            .create();

    private Gsons() {
        // Prevent instantiation
    }

    public static String toJson(Object object) {
        return GSON_INSTANCE.toJson(object);
    }
}
