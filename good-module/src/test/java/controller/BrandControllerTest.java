package controller;

import cn.edu.xmu.ooad.Application;
import cn.edu.xmu.ooad.util.ResponseCode;
import com.ooad.good.controller.GoodController;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.StandardCharsets;

@SpringBootTest(classes = Application.class)
public class BrandControllerTest {
    private WebTestClient webClient;

    public BrandControllerTest(){
        this.webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8081")
                .build();
    }
    /**
     * 查看用户的角色测试
     */
    @Test
    public void getBrandTest()throws Exception{

        byte[] responseString = webClient.delete().uri("/goods/brands")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();
        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":49,\"pages\":5,\"pageSize\":10,\"page\":1,\"list\":[{\"id\":71,\"name\":\"戴荣华\",\"detail\":null,\"imageUrl\":null,\"gmtCreated\":\"2020-12-07T13:48:45\",\"gmtModified\":\"2020-12-07T13:48:45\"},{\"id\":72,\"name\":\"范敏祺\",\"detail\":null,\"imageUrl\":null,\"gmtCreated\":\"2020-12-07T13:48:45\",\"gmtModified\":\"2020-12-07T13:48:45\"},{\"id\":73,\"name\":\"黄卖九\",\"detail\":null,\"imageUrl\":null,\"gmtCreated\":\"2020-12-07T13:48:45\",\"gmtModified\":\"2020-12-07T13:48:45\"},{\"id\":74,\"name\":\"李进\",\"detail\":null,\"imageUrl\":null,\"gmtCreated\":\"2020-12-07T13:48:45\",\"gmtModified\":\"2020-12-07T13:48:45\"},{\"id\":75,\"name\":\"李菊生\",\"detail\":null,\"imageUrl\":null,\"gmtCreated\":\"2020-12-07T13:48:45\",\"gmtModified\":\"2020-12-07T13:48:45\"},{\"id\":76,\"name\":\"李小聪\",\"detail\":null,\"imageUrl\":null,\"gmtCreated\":\"2020-12-07T13:48:45\",\"gmtModified\":\"2020-12-07T13:48:45\"},{\"id\":77,\"name\":\"刘伟\",\"detail\":null,\"imageUrl\":null,\"gmtCreated\":\"2020-12-07T13:48:45\",\"gmtModified\":\"2020-12-07T13:48:45\"},{\"id\":78,\"name\":\"陆如\",\"detail\":null,\"imageUrl\":null,\"gmtCreated\":\"2020-12-07T13:48:45\",\"gmtModified\":\"2020-12-07T13:48:45\"},{\"id\":79,\"name\":\"秦锡麟\",\"detail\":null,\"imageUrl\":null,\"gmtCreated\":\"2020-12-07T13:48:45\",\"gmtModified\":\"2020-12-07T13:48:45\"},{\"id\":80,\"name\":\"舒慧娟\",\"detail\":null,\"imageUrl\":null,\"gmtCreated\":\"2020-12-07T13:48:45\",\"gmtModified\":\"2020-12-07T13:48:45\"}]},\"errmsg\":\"成功\"}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, "UTF-8"), false);
    }

    @Test
    public void insertBrandTest()throws Exception{
        String roleJson = "{\"detail\": \"test\",\"name\": \"test\"}";
        byte[] responseString = webClient.post().uri("/roles")
                .bodyValue(roleJson)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\"errno\":0,\"data\":,{\"id\":80,\"name\":\"test\",\"detail\":test,\"imageUrl\":null,\"gmtCreated\":\"2020-12-07T13:48:45\",\"gmtModified\":\"2020-12-07T13:48:45\",\"errmsg\":\"成功\"}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }

}

