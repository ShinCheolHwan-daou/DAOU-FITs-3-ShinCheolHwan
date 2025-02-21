package com.example.myboardproject.myjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class MyJsonParser {
    static final ObjectMapper objectMapper = new ObjectMapper();

    public static String mapToJson(Map<String, Object> obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    public static Map<String, Object> jsonToMap(BufferedReader bufferedReader) throws IOException {
        return objectMapper.readValue(bufferedReader, Map.class);
    }
}
