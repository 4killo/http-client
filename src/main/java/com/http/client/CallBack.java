package com.http.client;


public interface CallBack {
    void failed(Throwable e);
    void completed(HttpResponse httpResponse);
}
