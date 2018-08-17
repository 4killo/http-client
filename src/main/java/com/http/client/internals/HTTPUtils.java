package com.http.client.internals;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


public interface HTTPUtils {

   //Logger LOGGER = LoggerFactory.getLogger(HTTPUtils.class);

   /**
    * This Method returns response status code , All response headers and body of the response.
    * If the response status is between 200 to 400 it will read from Connection InputStream other wise Connection ErrorStream.
    *
    * @param endPointURL:       endpoint Url to make call
    * @param connectionTimeout: connection time out
    * @param readTimeOut:       readTimeOut
    * @return response: returns response code, all response headers  and body
    */
   static Pair<Integer, String> sendRequest(String endPointURL,
                                            String params,
                                            int connectionTimeout,
                                            int readTimeOut,
                                            Map<String, String> requestMap) {
       String output;
       HttpURLConnection conn;
       BufferedReader br;
       StringBuilder buffer;
       OutputStreamWriter wr;
       int statusCode;
       try {
           conn = getConnection(endPointURL, connectionTimeout);
           requestMap.entrySet().forEach(i -> conn.setRequestProperty(i.getKey(), i.getValue()));
           wr = new OutputStreamWriter(conn.getOutputStream());
           wr.write(params);
           wr.flush();
           wr.close();
           conn.setReadTimeout(readTimeOut);

           statusCode = conn.getResponseCode();

           InputStream is = (statusCode >= HttpURLConnection.HTTP_OK
                   && statusCode < HttpURLConnection.HTTP_BAD_REQUEST)
                   ? conn.getInputStream() : conn.getErrorStream();
           br = new BufferedReader(new InputStreamReader(is));
           buffer = new StringBuilder();
           while ((output = br.readLine()) != null) {
               buffer.append(output);
           }
           br.close();
           output = buffer.toString();
       } catch (Exception e) {
           throw new RuntimeException(e);
       }

       return new Pair<>(statusCode, output);
   }

   static HttpURLConnection getConnection(String endPointURL, int connectionTimeout) {
       HttpURLConnection conn = null;
       try {
           URL url = new URL(endPointURL);
           conn = (HttpURLConnection) url.openConnection();
           conn.setDoOutput(true);
           conn.setConnectTimeout(connectionTimeout);
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
       return conn;
   }

   class Pair<K, V> implements Serializable {

       private K key;

       public K getKey() {
           return key;
       }

       private V value;

       public V getValue() {
           return value;
       }

       public Pair(K key, V value) {
           this.key = key;
           this.value = value;
       }

       @Override
       public String toString() {
           return key + "=" + value;
       }

       @Override
       public int hashCode() {
           return key.hashCode() * 13 + (value == null ? 0 : value.hashCode());
       }

       @Override
       public boolean equals(Object o) {
           if (this == o) return true;
           if (o instanceof Pair) {
               Pair pair = (Pair) o;
               if (key != null ? !key.equals(pair.key) : pair.key != null) return false;
               if (value != null ? !value.equals(pair.value) : pair.value != null) return false;
               return true;
           }
           return false;
       }
   }
}
