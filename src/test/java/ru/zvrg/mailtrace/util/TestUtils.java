package ru.zvrg.mailtrace.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TestUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private static final ObjectReader READER = MAPPER.reader();
    private static final ObjectWriter WRITER = MAPPER.writerWithDefaultPrettyPrinter();

    public static <T> String convertJsonFromFileToString(String filePath, Class<T> clazz) throws IOException {
        var object = convertJsonFromFileToObject(filePath, clazz);
        return WRITER.writeValueAsString(object);
    }

    public static <T> String convertJsonFromObjectToString(T object) throws IOException {
        return WRITER.writeValueAsString(object);
    }

    public static <T> T convertJsonFromStringToObject(String json, Class<T> clazz) throws IOException {
        return READER.readValue(json, clazz);
    }

    public static <T> T convertJsonFromFileToObject(String filePath, Class<T> clazz) throws IOException {
        return READER.readValue(new File(filePath), clazz);
    }

    public static <T> List<T> convertJsonFromFileToList(String filePath, Class<T> clazz) throws IOException {
        return MAPPER.readValue(new File(filePath), MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
    }

    public static <T> String convertJsonFromFileToStringList(String filePath, Class<T> clazz) throws IOException {
        return WRITER.writeValueAsString(MAPPER.readValue(new File(filePath), MAPPER.getTypeFactory().constructCollectionType(List.class, clazz)));
    }

    public static String readJsonFromFileWithoutAttributes(String filePath, String... attributes) throws IOException {
        Map<String, Object> map = MAPPER.readValue(new File(filePath), new TypeReference<Map<String, Object>>() {});
        removeAttributes(map, attributes);
        return WRITER.writeValueAsString(map);
    }

    private static void removeAttributes(Map<String, Object> map, String... attributes) {
        Arrays.stream(attributes).forEach(map::remove);
        for (Object value : map.values()) {
            if (value instanceof Map) {
                removeAttributes((Map<String, Object>) value, attributes);
            } else if (value instanceof List) {
                for (Object item : (List<?>) value) {
                    if (item instanceof Map) {
                        removeAttributes((Map<String, Object>) item, attributes);
                    }
                }
            }
        }
    }
}
