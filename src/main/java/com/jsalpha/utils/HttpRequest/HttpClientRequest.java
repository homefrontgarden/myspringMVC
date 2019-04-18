package com.jsalpha.utils.HttpRequest;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author dengjingsi
 * 发送请求客户端
 */
public class HttpClientRequest {
    /**
     * 把参数组装到url地址中
     * @param url
     * @param param
     * @return
     */
    private String getUrl(String url,Map<String,String> param) throws UnsupportedEncodingException {
        return url+getStringParam(param);
    }

    /**
     * 把param键值对，拼接成url参数形式
     * @param param
     * @return
     * @throws UnsupportedEncodingException
     */
    private String getStringParam(Map<String,String> param) throws UnsupportedEncodingException {
        if(null != param && param.size()>0){
            StringBuilder stringUrl = new StringBuilder("?");
            for(Map.Entry<String,String> entry : param.entrySet()){
                stringUrl.append(URLEncoder.encode(entry.getKey(),"UTF-8")).append('=').append(URLEncoder.encode(entry.getValue(),"UTF-8")).append('&');
            }
            return stringUrl.toString().substring(0,stringUrl.length()-1);
        }
        return "";
    }

//    /**
//     * get请求
//     * @param httpGet
//     * @return
//     * @throws IOException
//     */
//    public String get(HttpGet httpGet) throws IOException {
//        CloseableHttpClient httpclient = HttpClients.createDefault();
//        CloseableHttpResponse response = null;
//        String data;
//        response = httpclient.execute(httpGet);
//        HttpEntity httpEntity = response.getEntity();
//        data = EntityUtils.toString(httpEntity,"UTF-8");
//        EntityUtils.consume(httpEntity);
//        response.close();
//        return data;
//    }
//
//    /**
//     * post请求
//     * @param httpPost
//     * @param body
//     * @return
//     * @throws IOException
//     */
//    public String post(HttpPost httpPost,String body) throws IOException {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        if(null != body){
//            HttpEntity stringEntity = new StringEntity(body,"UTF-8");
//            httpPost.setEntity(stringEntity);
//        }
//        CloseableHttpResponse response = null;
//        String data;
//        response = httpClient.execute(httpPost);
//        HttpEntity httpEntity = response.getEntity();
//        data = EntityUtils.toString(httpEntity,"UTF-8");
//        EntityUtils.consume(httpEntity);
//        response.close();
//        return data;
//    }
//
//    /**
//     * put请求
//     * @param httpPut
//     * @param body
//     * @return
//     * @throws IOException
//     */
//    public String put(HttpPut httpPut, String body) throws IOException {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        if (null != body){
//            HttpEntity stringEntity = new StringEntity(body,"UTF-8");
//            httpPut.setEntity(stringEntity);
//        }
//        CloseableHttpResponse response = null;
//        String data;
//        response = httpClient.execute(httpPut);
//        HttpEntity httpEntity = response.getEntity();
//        data = EntityUtils.toString(httpEntity,"UTF-8");
//        EntityUtils.consume(httpEntity);
//        response.close();
//        return data;
//    }

    /**
     * 根据方法参数创建request请求
     * @param url
     * @param method
     * @param param
     * @return
     * @throws UnsupportedEncodingException
     */
    private HttpUriRequest getRequest(String url, String method, Map<String,String> param) throws UnsupportedEncodingException {
        url = getUrl(url,param);
        if("POST".equalsIgnoreCase(method)){
            return new HttpPost(url);
        }else if("GET".equalsIgnoreCase(method)){
            return new HttpGet(url);
        }else{
            return new HttpPut(url);
        }
    }
    /**
     * 发送客户端请求方法
     * @param url
     * @param param
     * @param method
     * @param body
     * @return
     * @throws IOException
     */
    private String request(String url, Map<String,String> param, String method, String body, String contentType) throws IOException {
//        url = getUrl(url,param);
        HttpUriRequest httpUriRequest = getRequest(url,method,param);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        if (contentType != null) {
            httpUriRequest.addHeader("Content-Type", contentType);
        }
        if (null != body){
            HttpEntity stringEntity = new StringEntity(body,"UTF-8");
            ((HttpEntityEnclosingRequestBase)httpUriRequest).setEntity(stringEntity);
        }
        CloseableHttpResponse response = null;
        String data;
        response = httpClient.execute(httpUriRequest);
        HttpEntity httpEntity = response.getEntity();
        data = EntityUtils.toString(httpEntity,"UTF-8");
        EntityUtils.consume(httpEntity);
        response.close();
        return data;
    }

    /**
     * post的json请求
     * @param url
     * @param param
     * @param body
     * @return
     * @throws IOException
     */
    public String postJson(String url,Map<String,String> param,String body) throws IOException {
        return request(url,param,"POST",body,"application/json");
    }

    /**
     * get请求
     * @param url
     * @param param
     * @return
     * @throws IOException
     */
    public String get(String url,Map<String,String> param) throws IOException {
        return request(url,param,"GET",null,"application/json");
    }
}
