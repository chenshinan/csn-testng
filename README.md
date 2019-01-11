# 自动化测试-testng

## TestNg是什么

TestNG是一个测试框架，其灵感来自JUnit和NUnit，但引入了一些新的功能，使其功能更强大，使用更方便。TestNG是一个开源自动化测试框架;TestNG表示下一代(Next Generation的首字母)。 TestNG类似于JUnit(特别是JUnit 4)，但它不是JUnit框架的扩展。它的灵感来源于JUnit。它的目的是优于JUnit，尤其是在用于测试集成多类时。使用`注解`的方式带来更便捷的测试编写

## TestNg的特点

- 丰富注解，如@Test、@BeforeClass、@Factory、@DataProvider等。
- TestNG使用Java和面向对象的功能支持综合类测试(例如，默认情况下，不用创建一个新的测试每个测试方法的类的实例)
- 独立的编译时测试代码和运行时配置/数据信息，修改无需重新编译测试代码
- 灵活的运行时配置
- 主要介绍“测试组”。当编译测试，只要要求TestNG运行所有的“前端”的测试，或“快”，“慢”，“数据库”等
- 支持依赖测试方法，并行测试，负载测试，局部故障
- 灵活的插件API
- 支持多线程测试，通过Parallel属性设置并发测试
- 可以生成HTML格式的测试报告。对测试结果的描述更加详细，方便定位错误

## TestNg能做什么

![image](https://csn-images.oss-cn-shenzhen.aliyuncs.com/markdown/20190110202915.png)

## TestNg的基本注解

![image](https://csn-images.oss-cn-shenzhen.aliyuncs.com/markdown/20190110203123.png)

* 注解执行顺序

![image](https://csn-images.oss-cn-shenzhen.aliyuncs.com/markdown/20190110204637.png)

## 实战

### pom配置

* 添加TestNg依赖

```xml
<dependency>
    <groupId>org.testng</groupId>
    <artifactId>testng</artifactId>
    <version>6.11</version>
    <!--<scope>test</scope>-->
</dependency>
```

* 配置测试的插件maven-surefire-plugin，并指定xml路径

```pom
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.0.0-M3</version>
            <configuration>
                <suiteXmlFiles>
                    <suiteXmlFile>src/test/resources/testng.xml</suiteXmlFile>
                </suiteXmlFiles>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### TestNg做API测试，整合RestTemplate做授权认证

* 增加http请求拦截器

```java
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
```

* 测试配置中注入restTemplate，先登录后，获取到token放入拦截器中

```java
@Configuration
public class TestConfiguration {
    @Autowired
    Environment environment;
    @Autowired
    LoginUtil loginUtil;
    @Bean
    public RestTemplate restTemplate(ApplicationContext applicationContext) {
        //设置http请求禁用重定向（这样才能获取到token）
        HttpClient httpClient = HttpClientBuilder.create().disableRedirectHandling().build();
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
        //登录授权获取token
        String accessToken = loginUtil.login(restTemplate);
        //设置认证拦截器
        HttpClientAuthInterceptor httpClientAuthInterceptor = new HttpClientAuthInterceptor(accessToken);
        restTemplate.setInterceptors(Collections.singletonList(httpClientAuthInterceptor));
        return restTemplate;
    }
}
```

* 编写restTemplate请求

```java
//设置请求头
HttpHeaders requestHeaders = new HttpHeaders();
requestHeaders.add("Cookie", "xx");
requestHeaders.add("content-type", "application/x-www-form-urlencoded");

//设置请求体，可以用MultiValueMap，实现一对多的key-value映射map
MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
requestBody.add("username", "root");
requestBody.add("password", "root");
HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

//restTemplate的exchange请求，可以为GET方式配置请求头的信息
ResponseEntity<Object> clientOauth = restTemplate.exchange(url, HttpMethod.GET, authRequest, Object.class);
```

### TestNg整合springboot

要在SpringBoot中使用TestNg需要`继承AbstractTestNGSpringContextTests`，AbstractTestNGSpringContextTests在执行测试之前先注入spring的相关内容

```java
@SpringBootTest(classes = {CsnTestngApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ConfigProperty configProperty;

    @Test
    public void userSelf() {
        String url = configProperty.apiGateway + "/iam/v1/users/self";
        ResponseEntity<Object> responseEntity = restTemplate.getForEntity(url, Object.class);
        System.out.println(responseEntity.getBody());
    }
}
```

### TestNg整合ReportNG

TestNG自带html格式的测试报告，可以集成到一些持续集成框架中，但是自带的测试报告内容太过简单，而ReportNG正是对TestNG测试报告进行增强的一个插件

* 添加ReportNG依赖

```xml
<!-- 依赖reportNg-->
<dependency>
    <groupId>org.uncommons</groupId>
    <artifactId>reportng</artifactId>
    <version>1.1.4</version>
    <scope>test</scope>
    <exclusions>
        <exclusion>
            <groupId>org.testng</groupId>
            <artifactId>testng</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<!-- 依赖Guice -->
<dependency>
    <groupId>com.google.inject</groupId>
    <artifactId>guice</artifactId>
    <version>3.0</version>
    <scope>test</scope>
</dependency>
```

* maven测试插件添加ReportNG配置

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <suiteXmlFiles>
           <suiteXmlFile>src/test/resources/testng.xml</suiteXmlFile>
        </suiteXmlFiles>
        <!-- ReportNG配置 -->
        <properties>
            <property>
                <name>usedefaultlisteners</name>
                <value>false</value>
            </property>
            <property>
                <name>listener</name>
                <value>org.uncommons.reportng.HTMLReporter</value>
            </property>
        </properties>
        <workingDirectory>target/</workingDirectory>
    </configuration>
</plugin>
```

> 通过maven运行测试，生成测试报告路径为：/target/surefire-reports/html/index.html

### testng.xml配置

```xml
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd" >
<suite name="suite1" verbose="1">
    <test name="test1">
        <classes>
            <class name="com.chenshinan.testng.ApiTest"/>
        </classes>
    </test>
</suite>
```

### 执行测试

* maven

```
mvn test
```

## TestNg自定义功能

TestNG支持在测试时附加自定义的listener, reporter, annotation transformer, method interceptor。默认会使用基本的listener生成HTML和XML报告。`可以利用这些自定义结果报表`

* Listener实现org.testng.ITestListener接口,会在测试开始、通过、失败等时刻实时发送通知

* Reporter实现org.testng.IReporter接口，在整个测试运行完毕之后才会发送通知，参数为对象列表，包含整个测试的执行结果状况

参考文献：
- [官方文档](https://testng.org/doc/documentation-main.html)
- [易百教程](https://www.yiibai.com/testng/basic-annotations.html)
- [IBM教程：如何使用TestNg](https://www.ibm.com/developerworks/cn/java/j-testng/index.html)
- [Maven-Surefire-Plugin插件整合TestNg](https://maven.apache.org/surefire/maven-surefire-plugin/examples/testng.html)