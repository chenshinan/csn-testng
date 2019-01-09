package com.chenshinan.testng.config;

import com.chenshinan.testng.interceptor.HttpClientAuthInterceptor;
import com.chenshinan.testng.utils.LoginUtil;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * @author shinan.chen
 * @since 2019/1/8
 */
@Configuration
public class TestConfiguration {
    @Autowired
    Environment environment;
    @Autowired
    LoginUtil loginUtil;

    @Bean
    public RestTemplate restTemplate(ApplicationContext applicationContext) {
        HttpClient httpClient = HttpClientBuilder.create().disableRedirectHandling().build();
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
        String accessToken = loginUtil.login(restTemplate);
        HttpClientAuthInterceptor httpClientAuthInterceptor = new HttpClientAuthInterceptor(accessToken);
        restTemplate.setInterceptors(Collections.singletonList(httpClientAuthInterceptor));
        return restTemplate;
    }


}
