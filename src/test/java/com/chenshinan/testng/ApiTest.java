package com.chenshinan.testng;

import com.chenshinan.testng.config.ConfigProperty;
import com.chenshinan.testng.utils.EncodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.testng.annotations.Test;

/**
 * @author shinan.chen
 * @since 2019/1/8
 */

@SpringBootTest(classes = {CsnTestngApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiTest extends AbstractTestNGSpringContextTests {
    private static final Logger logger = LoggerFactory.getLogger(ApiTest.class);
//
//    @Bean
//    public TestRestTemplate restTemplate(ApplicationContext applicationContext) {
//        TestRestTemplate rest = new TestRestTemplate();
////        LocalHostUriTemplateHandler handler = new LocalHostUriTemplateHandler(
////                environment, "http");
////        rest.setUriTemplateHandler(handler);
//        return rest;
//    }

    @Autowired
    private ConfigProperty configProperty;
    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${testng.username:choerodon}")
    String name;

    @Test
    public void login() {

//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.add("username", testConfiguration.username);
//        map.add("password", testConfiguration.password);
        //获取客户端cookie
        String url = configProperty.apiGateway + "/oauth/oauth/authorize?scope=default" +
                "&redirect_uri=http://api.staging.saas.hand-china.com&response_type=token&realm=default&state=client&client_id=client";
        ResponseEntity<Object> clientOauth = restTemplate.getForEntity(url, null);
        String cookie = clientOauth.getHeaders().get("set-cookie").get(0).split(";")[0];
        //根据客户端cookie获取用户token
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", cookie);
        requestHeaders.add("content-type", "application/x-www-form-urlencoded");
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("username", configProperty.username);
        requestBody.add("password", EncodeUtil.encodePassword(configProperty.password));
        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(requestBody, requestHeaders);
        ResponseEntity<Object> userOauth = restTemplate.postForEntity(configProperty.apiGateway + "/oauth/login", requestEntity, Object.class);
        System.out.println(requestBody.get("password"));
        cookie = userOauth.getHeaders().get("set-cookie").get(0).split(";")[0];
        //第三步
//        requestHeaders.add("Cookie", cookie);
//        requestEntity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<Object> xx = restTemplate.getForEntity(url, null);
        System.out.println(userOauth);

        //        authorize = ''
//        //添加cookie以保持状态
//        HttpHeaders headers = new HttpHeaders();
//        String headerValue = responseEntity.getHeaders().get("Set-Cookie").toString().replace("[", "");
//        headerValue = headerValue.replace("]", "");

    }

    @Test
    public void trim() {
        assert "foo".equals(StringUtils.trimAllWhitespace("  foo   "));
    }


}
