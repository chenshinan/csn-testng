package com.chenshinan.testng.utils;

import com.chenshinan.testng.config.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author shinan.chen
 * @since 2019/1/9
 */
@Component
public class LoginUtil {
    public static final Logger logger = LoggerFactory.getLogger(LoginUtil.class);
    @Autowired
    private ConfigProperty configProperty;

    public String login(RestTemplate restTemplate) {
        String accessToken = null;
        try {
            //获取客户端cookie
            String url = configProperty.apiGateway + "/oauth/oauth/authorize?scope=default" +
                    "&redirect_uri=http://api.staging.saas.hand-china.com&response_type=token&realm=default&state=client&client_id=client";
            //模拟用户登录，token返回到重定向的body中，由于没有重定向，则无法直接获取到token
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.add("content-type", "application/x-www-form-urlencoded");
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("username", configProperty.username);
            //密码加密
            String password = EncodeUtil.encodePassword(configProperty.password);
            requestBody.add("password", password);
            HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(requestBody, requestHeaders);
            ResponseEntity<Object> userOauth = restTemplate.postForEntity(configProperty.apiGateway + "/oauth/login", requestEntity, Object.class);
            //获取当前登录的cookie
            String cookie = userOauth.getHeaders().get("set-cookie").get(0).split(";")[0];
            //再访问author，简化模式从session判断你已经登录，拿token redirect到client地址带上token，这时候禁用redirect就可以拿到token
            requestHeaders.add("Cookie", cookie);
            HttpEntity<String> authRequest = new HttpEntity<>(null, requestHeaders);
            ResponseEntity<Object> clientOauth = restTemplate.exchange(url, HttpMethod.GET, authRequest, Object.class);
            String location = clientOauth.getHeaders().get("location").get(0);
            accessToken = location.split("#access_token=")[1].split("&token_type")[0];
            System.out.println("accessToken:" + accessToken);
        } catch (Exception e) {
            logger.error("登录认证失败");
            e.printStackTrace();
        }
        return accessToken;
    }
}
