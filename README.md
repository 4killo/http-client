# http-client


The http client can be used for sync and aysnc call. For async  it uses Java 8 CompletableFuture  and wait as  call back.

** Usage: Sync Example 1**


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
    
    
** Usage: Sync Example 2**

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
    
    
    
    ** Usage: async Example 1**
    
    
    public void testAsJsonAsync() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        HttpClient.post("https://xxxxxxxxxxxxxxx")
                .body(getParam2())
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .connectionTimeout(100)
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

      latch.await(1000, TimeUnit.MILLISECONDS);


    }
    
    


