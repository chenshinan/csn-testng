package com.chenshinan.testng.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author shinan.chen
 * @since 2019/1/8
 */
@ConfigurationProperties("testng")
@Component
public class ConfigProperty {
    public String username;
    public String password;
    public String apiGateway;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApiGateway() {
        return apiGateway;
    }

    public void setApiGateway(String apiGateway) {
        this.apiGateway = apiGateway;
    }
}
