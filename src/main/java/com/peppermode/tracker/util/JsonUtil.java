package com.peppermode.tracker.util;

import com.google.gson.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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

    public static <T> List<T> readList(Path path, Class<T[]> arrayClass) {
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }

        try (Reader reader = Files.newBufferedReader(path)) {
            T[] arr = gson().fromJson(reader, arrayClass);
            if (arr == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(Arrays.asList(arr));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON from " + path, e);
        }
    }

    public static <T> void writeList(Path path, List<T> list) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directories for " + path, e);
        }

        try (Writer writer = Files.newBufferedWriter(path)) {
            gson().toJson(list, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write JSON to " + path, e);
        }
    }

}

