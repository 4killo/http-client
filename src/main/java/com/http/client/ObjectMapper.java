package com.http.client;



public interface ObjectMapper {
    String writeValue(Object object);
    <T> T readValue(String value, Class<T> valueType);
}
