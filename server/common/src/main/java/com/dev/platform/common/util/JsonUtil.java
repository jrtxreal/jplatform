package com.dev.platform.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class JsonUtil {
    /**
     * java对象转json字符串
     *
     * @param o 待转换的java对象
     * @return
     */
    public static String toJson(Object o) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        String json = objectMapper.writeValueAsString(o);
        return json;
    }

    /**
     * json字符串转java对象
     *
     * @param tClass 对象所属的类
     * @param json   json字符串
     * @param <T>
     * @return
     */
    public static <T> T fromJson(Class<T> tClass, String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        T object = objectMapper.readValue(json, tClass);
        return object;
    }


    /**
     * json字符串转java对象
     *
     * @param tClass 对象所属的类
     * @param json   json字符串
     * @return
     */
    public static <T> List<T> toListObject(Class<T> tClass, String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, tClass);
        return objectMapper.readValue(json, javaType);
    }


    /**
     * json字符串转 JsonNode对象
     * @param json json字符串
     * @return
     * @throws IOException
     */
    public static JsonNode toJsonNode(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(json);
    }
}
