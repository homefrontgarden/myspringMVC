package com.jsalpha.utils;

import com.jsalpha.utils.HttpRequest.HttpClientRequest;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

public class TestHttpClientRequest {
    @Test
    public void test(){
        String data;
        HttpClientRequest httpClientRequest = new HttpClientRequest();
        try {
            data = httpClientRequest.get("http://172.16.1.5:8085/calcPentyValueItems?taskId=3127459475161088",new HashMap<>());
            System.out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("2");
        }finally {
            System.out.println("3");
        }
    }
    @Test
    public void testPost(){
        String data;
        HttpClientRequest httpClientRequest = new HttpClientRequest();
        try {
            data = httpClientRequest.postJson("http://localhost:8187/examination/student/execute",null,"");
            System.out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("2");
        }finally {
            System.out.println("3");
        }
    }
}
