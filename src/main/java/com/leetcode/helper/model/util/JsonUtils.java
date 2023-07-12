package com.leetcode.helper.model.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class JsonUtils {

    private final static ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

    }

    public static String toJSONString(Object object) throws Exception {
        return OBJECT_MAPPER.writeValueAsString(object);
    }

    public static <T> T parseObject(String text, Class<T> clazz) throws Exception {
        return OBJECT_MAPPER.readValue(text, clazz);
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) throws Exception {
        return OBJECT_MAPPER.readValue(text, OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
    }
}
