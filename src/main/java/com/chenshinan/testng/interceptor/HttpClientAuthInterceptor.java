package com.chenshinan.testng.interceptor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author shinan.chen
 * @since 2019/1/9
 */
public class HttpClientAuthInterceptor implements ClientHttpRequestInterceptor {
    private final String accessToken;

    public HttpClientAuthInterceptor(String accessToken) {
        this.accessToken = accessToken!=null?accessToken:"";
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");
        return execution.execute(request, body);
    }
}
