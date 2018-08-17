package com.http.client;



public class HttpResponse<T> {

    private T body;
    private int responseCode;

    public HttpResponse() {
    }

    public HttpResponse(int responseCode ,T body) {
        this.responseCode = responseCode;
        this.body = body;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
