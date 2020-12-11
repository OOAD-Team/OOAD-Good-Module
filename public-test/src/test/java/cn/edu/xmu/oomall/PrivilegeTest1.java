package cn.edu.xmu.oomall;

import cn.edu.xmu.ooad.Application;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.ResponseCode;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Xianwei Wang
 * created at 11/30/20 12:27 PM
 * @detail cn.edu.xmu.oomall
 */
@SpringBootTest(classes = Application.class)   //标识本类是一个SpringBootTest
public class PrivilegeTest1 {
    private WebTestClient webClient;

    public PrivilegeTest1(){
        this.webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
    }

    /**
     * 查看用户的角色测试1
     * @throws Exception
     * @author Xianwei Wang
     */
    @Test
    public void getUserRoleTest1() throws Exception {
        String token = this.login("13088admin", "123456");

        byte[] responseString = webClient.get().uri("/shops/0/adminusers/47/roles").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBodyContent();
        String expectedResponse = "{\"errno\":0,\"data\":[{\"id\":78,\"user\":{\"id\":47,\"userName\":\"2721900002\"},\"role\":{\"id\":85,\"name\":\"总经办\"},\"creator\":{\"id\":1,\"userName\":\"13088admin\"},\"gmtCreate\":\"2020-11-01T09:48:24\"}],\"errmsg\":\"成功\"}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, "UTF-8"), true);

    }

    /**
     * 查看用户的角色测试2
     * @throws Exception
     * @author Xianwei Wang
     */
    @Test
    public void getUserRoleTest2() throws Exception {
        String token = this.login("13088admin", "123456");

        byte[] responseString = webClient.get().uri("/shops/2/adminusers/49/roles").header("authorization",token)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBodyContent();
        String expectedResponse = "{\n" +
                "    \"errno\": 504,\n" +
                "    \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, "UTF-8"), true);

    }

    /**
     * 查看用户的角色测试3
     * @throws Exception
     * @author Xianwei Wang
     */
    @Test
    public void getUserRoleTest3() throws Exception {
        String token = this.login("13088admin", "123456");

        byte[] responseString = webClient.get().uri("/shops/0/adminusers/100/roles").header("authorization",token)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBodyContent();
        String expectedResponse = "{\n" +
                "    \"errno\": 504,\n" +
                "    \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, "UTF-8"), true);

    }

    /**
     * 查看自己的角色测试1
     * @throws Exception
     * @author Xianwei Wang
     */
    @Test
    public void getSelfUserRoleTest1() throws Exception {
        String token = this.login("13088admin", "123456");

        byte[] responseString = webClient.get().uri("/adminusers/self/roles").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBodyContent();
        String expectedResponse = "{\n" +
                "    \"errno\": 0,\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"id\": 76,\n" +
                "            \"user\": {\n" +
                "                \"id\": 1,\n" +
                "                \"userName\": \"13088admin\"\n" +
                "            },\n" +
                "            \"role\": {\n" +
                "                \"id\": 23,\n" +
                "                \"name\": \"管理员\"\n" +
                "            },\n" +
                "            \"creator\": {\n" +
                "                \"id\": 1,\n" +
                "                \"userName\": \"13088admin\"\n" +
                "            },\n" +
                "            \"gmtCreate\": \"2020-11-01T09:48:24\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, "UTF-8"), true);

    }

    /**
     * 查看自己的角色测试2
     * @throws Exception
     * @author Xianwei Wang
     */
    @Test
    public void getSelfUserRoleTest2() throws Exception {
        String token = this.login("8532600003", "123456");
        byte[] responseString = webClient.get().uri("/adminusers/self/roles").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBodyContent();
        String expectedResponse = "{\n" +
                "    \"errno\": 0,\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"id\": 79,\n" +
                "            \"user\": {\n" +
                "                \"id\": 48,\n" +
                "                \"userName\": \"8532600003\"\n" +
                "            },\n" +
                "            \"role\": {\n" +
                "                \"id\": 86,\n" +
                "                \"name\": \"库管\"\n" +
                "            },\n" +
                "            \"creator\": {\n" +
                "                \"id\": 1,\n" +
                "                \"userName\": \"13088admin\"\n" +
                "            },\n" +
                "            \"gmtCreate\": \"2020-11-01T09:48:24\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, "UTF-8"), true);

    }

    /**
     * 赋予用户角色测试1
     * @throws Exception
     * @author Xianwei Wang
     */
    @Test
    public void assignRoleTest1() throws Exception {
        String token = this.login("8532600003", "123456");
        byte[] responseString = webClient.post().uri("/shops/0/adminusers/47/roles/1000").header("authorization",token)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBodyContent();
        String expectedResponse = "{\n" +
                "    \"errno\": 504,\n" +
                "    \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, "UTF-8"), true);

    }

    /**
     * 赋予用户角色测试2
     * @throws Exception
     * @author Xianwei Wang
     */
    @Test
    public void assignRoleTest2() throws Exception {
        String token = this.login("8532600003", "123456");
        byte[] responseString = webClient.post().uri("/shops/0/adminusers/10000/roles/84").header("authorization",token)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBodyContent();
        String expectedResponse = "{\n" +
                "    \"errno\": 504,\n" +
                "    \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, "UTF-8"), true);

    }

//    /**
//     * 赋予用户角色测试3
//     * @throws Exception
//     * @author Xianwei Wang
//     */
//    @Test
//    public void assignRoleTest3() throws Exception {
//        String token = this.login("8532600003", "123456");
//        byte[] responseString = webClient.post().uri("/shops/0/adminusers/47/roles/84").header("authorization",token)
//                .exchange()
//                .expectStatus().isOk()
//                .expectHeader().contentType("application/json;charset=UTF-8")
//                .expectBody()
//                .returnResult().getResponseBodyContent();
//        String expectedResponse = "{\n" +
//                "    \"errno\": 0,\n" +
//                "    \"data\": {\n" +
//                "        \"id\": 91,\n" +
//                "        \"user\": {\n" +
//                "            \"id\": 47,\n" +
//                "            \"userName\": \"2721900002\"\n" +
//                "        },\n" +
//                "        \"role\": {\n" +
//                "            \"id\": 84,\n" +
//                "            \"name\": \"文案\"\n" +
//                "        },\n" +
//                "        \"creator\": {\n" +
//                "            \"id\": 48,\n" +
//                "            \"userName\": \"8532600003\"\n" +
//                "        },\n" +
//                "        \"gmtCreate\": \"2020-11-30T13:19:45.909191\"\n" +
//                "    },\n" +
//                "    \"errmsg\": \"成功\"\n" +
//                "}";
//        JSONAssert.assertEquals(expectedResponse, new String(responseString, "UTF-8"), false);
//
//    }

    /**
     * 赋予用户角色测试3
     * @throws Exception
     * @author Xianwei Wang
     */
    @Test
    public void assignRoleTest3() throws Exception {
        String token = this.login("8532600003", "123456");
        byte[] responseString = webClient.post().uri("/shops/0/adminusers/48/roles/86").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBodyContent();
        String expectedResponse = "{\"errno\":737,\"errmsg\":\"用户已拥有该角色\"}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, "UTF-8"), true);

    }

    /**
     * 取消用户角色测试1 成功
     * @throws Exception
     * @author Xianwei Wang
     */
    @Test
    public void revokeRoleTest1() throws Exception {
        String token = this.login("13088admin", "123456");
        byte[] responseString = webClient.delete().uri("/shops/0/adminusers/50/roles/87").header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBodyContent();
        String expectedResponse = "{\n" +
                "    \"errno\": 0,\n" +
                "    \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, "UTF-8"), false);
    }

    /**
     * 取消用户角色测试2:角色不存在
     * @throws Exception
     * @author Xianwei Wang
     */
    @Test
    public void revokeRoleTest2() throws Exception {
        String token = this.login("13088admin", "123456");
        byte[] responseString = webClient.delete().uri("/shops/0/adminusers/50/roles/1000").header("authorization", token)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBodyContent();
        String expectedResponse = "{\n" +
                "    \"errno\": 504,\n" +
                "    \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, "UTF-8"), false);
    }

    /**
     * 取消用户角色测试3:用户不存在
     * @throws Exception
     * @author Xianwei Wang
     */
    @Test
    public void revokeRoleTest3() throws Exception {
        String token = this.login("13088admin", "123456");
        byte[] responseString = webClient.delete().uri("/shops/0/adminusers/10000/roles/87").header("authorization", token)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBodyContent();
        String expectedResponse = "{\n" +
                "    \"errno\": 504,\n" +
                "    \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, "UTF-8"), false);
    }

    /**
     * 取消用户角色测试4:角色无该用户
     * @throws Exception
     * @author Xianwei Wang
     */
    @Test
    public void revokeRoleTest4() throws Exception {
        String token = this.login("13088admin", "123456");
        byte[] responseString = webClient.delete().uri("/shops/0/adminusers/57/roles/84").header("authorization", token)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBodyContent();
        String expectedResponse = "{\n" +
                "    \"errno\": 504,\n" +
                "    \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, "UTF-8"), false);
    }
    private String login(String userName, String password) throws Exception{
        LoginVo vo = new LoginVo();
        vo.setUserName(userName);
        vo.setPassword(password);
        String requireJson = JacksonUtil.toJson(vo);

        byte[] ret = webClient.post().uri("/privileges/login").bodyValue(requireJson).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        return  JacksonUtil.parseString(new String(ret, "UTF-8"), "data");

    }
}
