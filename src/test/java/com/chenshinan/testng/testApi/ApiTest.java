package com.chenshinan.testng.testApi;

import com.chenshinan.testng.CsnTestngApplication;
import com.chenshinan.testng.config.ConfigProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.client.RestTemplate;
import org.testng.Reporter;
import org.testng.annotations.Test;

/**
 * @author shinan.chen
 * @since 2019/1/8
 */

@SpringBootTest(classes = {CsnTestngApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ConfigProperty configProperty;

    @Test
    public void userSelf() {
        Reporter.log("输入参数："+1);
        String url = configProperty.apiGateway + "/iam/v1/users/self";
        ResponseEntity<Object> responseEntity = restTemplate.getForEntity(url, Object.class);
        System.out.println(responseEntity.getBody());
    }
}
