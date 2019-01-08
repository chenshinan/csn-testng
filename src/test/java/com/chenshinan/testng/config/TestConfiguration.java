package com.chenshinan.testng.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author shinan.chen
 * @since 2019/1/8
 */
@Configuration
public class TestConfiguration {
    @Autowired
    Environment environment;

    @Bean
    public TestRestTemplate restTemplate(ApplicationContext applicationContext) {
        TestRestTemplate rest = new TestRestTemplate();
        LocalHostUriTemplateHandler handler = new LocalHostUriTemplateHandler(
                environment, "http");
        rest.setUriTemplateHandler(handler);
//        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//        HttpClient httpClient = HttpClientBuilder.create()
//                .setRedirectStrategy(new LaxRedirectStrategy())
//                .build();
//        factory.setHttpClient(httpClient);
//        rest.
        return rest;
    }
}
