package com.test;


import com.http.client.CallBack;
import com.http.client.HttpClient;
import com.http.client.HttpResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class HttpClientTest {
    @Test
    public void testSendStringPodt() {

        String response =
                HttpClient.post("https://xxxxxxxxxxxxxxx")
                        .body(getParam1())
                        .header("accept", "application/json")
                        .header("Content-Type", "application/json")
                        .connectionTimeout(1000)
                        .asJson();

        System.out.print(response);
    }


    @Test
    public void testSendObject() throws IOException {

        HttpClient.setSerializer(x -> {
            try {
                return new ObjectMapper().writeValueAsString(x);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        String response =
                HttpClient.post("https://xxxxxxxxxxxxxxx")
                        .body(getFavoriteStoreRequest())
                        .header("accept", "application/json")
                        .header("Content-Type", "application/json")
                        .connectionTimeout(1000)
                        .asJson();

        System.out.print(response);
    }

    @Test
    public void testSendAndReadObject() throws IOException {

        HttpClient.setObjectMapper(new com.http.client.ObjectMapper() {
            private ObjectMapper jacksonObjectMapper = new ObjectMapper();

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        HttpResponse response =
                HttpClient.post("https://xxxxxxxxxxxxxxx")
                        .body(getParam2())
                        .header("accept", "application/json")
                        .header("Content-Type", "application/json")
                        .connectionTimeout(1000)
                        .readTimeOut(1000)
                        .asObject(Stores.class);

        System.out.print(response.getBody());
    }


    @Test
    public void testAsJsonAsync() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        HttpClient.post("https://xxxxxxxxxxxxxxx")
                .body(getParam2())
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .connectionTimeout(1000)
                .asJsonAsync(new CallBack() {
                    @Override
                    public void failed(Throwable e) {
                        //handle exception
                        System.out.print("Exception from AsJsonAsync " + e.getMessage());
                    }

                    @Override
                    public void completed(HttpResponse httpResponse) {
                        System.out.print("Response from AsJsonAsync " + httpResponse.getBody());
                    }
                });

      latch.await(10000, TimeUnit.MILLISECONDS);


    }

    private String getParam2() {
        return "{\n" +
                "  \"brandId\": \"VS\",\n" +
                "  \"emailId\": \"perf1988@lb.com\"\n" +
                "}";
    }


    private String getParam1() {
        return "{\n" +
                "  \"brandId\": \"VS\",\n" +
                "  \"emailId\": \"perf1988@lb.com\",\n" +
                "  \"stores\": [\n" +
                "    \"99430\"\n" +
                "  ]\n" +
                "}\n";
    }


    private FavoriteStoreRequest getFavoriteStoreRequest() throws IOException {
        return new ObjectMapper().readValue(getParam1(), FavoriteStoreRequest.class);
    }
}
