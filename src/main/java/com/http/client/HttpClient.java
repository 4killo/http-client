package com.http.client;



import com.http.client.internals.F1;
import com.http.client.internals.HTTPUtils;
import org.codehaus.jackson.JsonNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class HttpClient {
    private String url;
    private String param;
    private int connectionTimeout;
    private int readTimeOut;
    private Map<String, String> headers = new HashMap<>();
    private static volatile F1 f1;
    private static volatile ObjectMapper objectMapper;


    private HttpClient(String url) {
        this.url = url;
    }

    public static HttpClient post(String url) {
        return new HttpClient(url);
    }

    public static HttpClient get(String url) {
        return new HttpClient(url);
    }

    public HttpClient body(String param1) {
        this.param = param1;
        return this;
    }

    public HttpClient body(JsonNode jsonNode) {
        this.param = objToJson(jsonNode);
        return this;
    }

    public HttpClient body(Object object) {
        this.param = f1.writeValue(object);
        return this;
    }


    public HttpClient connectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public HttpClient readTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public HttpClient headers(Map<String, String> requestMap) {
        this.headers = requestMap;
        return this;
    }

    public HttpClient header(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public String asJson() {
        return HTTPUtils.sendRequest(url, param, connectionTimeout, readTimeOut, headers).getValue();
    }

    public void asJsonAsync(CallBack callBack) {
        CompletableFuture.supplyAsync(() ->
                HTTPUtils.sendRequest(url, param, connectionTimeout, readTimeOut, headers))
                .thenApply(pair -> new HttpResponse<>(pair.getKey(), pair.getValue()))
                .thenAccept(callBack::completed)
                .exceptionally(ex ->
                {
                    callBack.failed(ex);
                    return null;
                });

    }

    public HttpResponse asJsonWithResponseCode() {
        HTTPUtils.Pair<Integer, String> pair = HTTPUtils
                .sendRequest(url, param, connectionTimeout, readTimeOut, headers);
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setResponseCode(pair.getKey());
        httpResponse.setBody(pair.getValue());
        return httpResponse;
    }

    public HttpResponse asObject(Class clazz) {
        HTTPUtils.Pair<Integer, String> pair = HTTPUtils.sendRequest(url, param, connectionTimeout, readTimeOut, headers);
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setResponseCode(pair.getKey());
        httpResponse.setBody(objectMapper.readValue(pair.getValue(), clazz));
        return httpResponse;

    }

    public static void setSerializer(F1 f) {
        HttpClient.f1 = f;
    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        HttpClient.objectMapper = objectMapper;
    }


    private String objToJson(Object obj) {
        try {
            return new org.codehaus.jackson.map.ObjectMapper().writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
