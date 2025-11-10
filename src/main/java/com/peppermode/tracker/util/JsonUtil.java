package com.peppermode.tracker.util;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class JsonUtil {
    private JsonUtil() {}

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private static final JsonSerializer<LocalDateTime> LDT_SER = (src, typeOfSrc, context) ->
            new JsonPrimitive(ISO.format(src));

    private static final JsonDeserializer<LocalDateTime> LDT_DESER = (json, typeOfT, context) ->
            LocalDateTime.parse(json.getAsString(), ISO);

    public static Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, LDT_SER)
                .registerTypeAdapter(LocalDateTime.class, LDT_DESER)
                .setPrettyPrinting()
                .create();
    }
}

