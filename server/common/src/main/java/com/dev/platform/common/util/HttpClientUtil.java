package com.dev.platform.common.util;

import java.net.Authenticator;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.Map;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/6/2 14:03
 */
public class HttpClientUtil {
    // <创建http客户端>
    public static HttpClient buildClient(Long timeout) {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1) // 遵循HTTP协议的1.1版本
                .followRedirects(HttpClient.Redirect.NORMAL) // 正常的重定向
                .connectTimeout(Duration.ofMillis(timeout)) // 连接的超时时间为5秒
                //.authenticator(Authenticator.getDefault()) // 默认的身份认证
                .build();
        return client;
    }

    // <创建httpPost请求>
    public static HttpRequest buildPostRequest(String url, String body, Long readTimeout) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)) // 待调用的url地址
                .POST(HttpRequest.BodyPublishers.ofString(body)) // 调用方式为POST，且请求报文为字符串
                .header("Content-Type", "application/json") // 设置头部参数，内容类型为json
                .timeout(Duration.ofMillis(readTimeout))
                .build();
        return request;
    }

    // <创建httpPut请求>
    public static HttpRequest buildPutRequest(String url, String body, Long readTimeout) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)) // 待调用的url地址
                .PUT(HttpRequest.BodyPublishers.ofString(body)) // 调用方式为Put，且请求报文为字符串
                .header("Content-Type", "application/json") // 设置头部参数，内容类型为json
                .timeout(Duration.ofMillis(readTimeout))
                .build();
        return request;
    }

    // <创建httpDelete请求>
    public static HttpRequest buildDELETERequest(String url, Long readTimeout) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)) // 待调用的url地址
                .DELETE() // 调用方式为DELETE，且请求报文为字符串
                .timeout(Duration.ofMillis(readTimeout))
                .build();
        return request;
    }

    // <创建httpGet请求>
    public static HttpRequest buildGetRequest(String url, Long readTimeout) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)) // 待调用的url地址
                .GET() // 调用方式为Get请求
                .timeout(Duration.ofMillis(readTimeout))
                .build();
        return request;
    }
}
