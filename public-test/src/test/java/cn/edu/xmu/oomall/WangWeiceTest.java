package cn.edu.xmu.oomall;

import cn.edu.xmu.ooad.Application;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

/**
 * 角色公开测试
 *
 * @author 王纬策
 * @date 2020/11/30 12:28
 */
@SpringBootTest(classes = Application.class)   //标识本类是一个SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WangWeiceTest {

    private final WebTestClient webClient;

    public WangWeiceTest() {
        this.webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
    }

    /**
     * 管理员登录成功
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(0)
    public void adminLoginSuccess() throws Exception {
        String requireJson = "{\"userName\":\"13088admin\",\"password\":\"123456\"}";
        byte[] ret = webClient.post().uri("/privileges/login").bodyValue(requireJson).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 管理员登录失败
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(0)
    public void adminLoginFail() throws Exception {
        String requireJson = "{\"userName\":\"13088admin\",\"password\":\"12345\"}";
        byte[] ret = webClient.post().uri("/privileges/login").bodyValue(requireJson).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AUTH_INVALID_ACCOUNT.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.AUTH_INVALID_ACCOUNT.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 未登录查询角色信息
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(1)
    public void selectRoleTest1() throws Exception {
        byte[] ret = webClient.get().uri("/shops/0/roles?page=1&pageSize=2")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AUTH_NEED_LOGIN.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.AUTH_NEED_LOGIN.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 使用伪造token查询角色信息
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(1)
    public void selectRoleTest2() throws Exception {
        byte[] ret = webClient.get().uri("/shops/0/roles?page=1&pageSize=2")
                .header("authorization", "test")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AUTH_INVALID_JWT.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.AUTH_INVALID_JWT.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 管理员查询所有角色信息
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(1)
    public void selectRoleTest3() throws Exception {
        String token = this.creatTestToken(1L, 0L, 100);
        byte[] responseString = webClient.get().uri("/shops/{shopId}/roles?page=1&pageSize=2", 0)
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":2,\"pages\":1,\"pageSize\":2,\"page\":1,\"list\":[{\"id\":23,\"name\":\"管理员\",\"desc\":\"超级管理员，所有权限都有\",\"createdBy\":1,\"departId\":0,\"gmtCreate\":\"2020-11-01T09:48:24\",\"gmtModified\":\"2020-11-01T09:48:24\"},{\"id\":80,\"name\":\"财务\",\"desc\":null,\"createdBy\":1,\"departId\":0,\"gmtCreate\":\"2020-11-01T09:48:24\",\"gmtModified\":\"2020-11-01T09:48:24\"}]},\"errmsg\":\"成功\"}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 查询角色失败 高级查低级
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(1)
    public void selectRoleTest4() throws Exception {
        String token = this.creatTestToken(1L, 0L, 100);
        byte[] responseString = webClient.get().uri("/shops/{shopId}/roles?page=1&pageSize=2", 1)
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.errmsg").isEqualTo("部门id不匹配：1")
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 查询角色失败 低级查高级
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(1)
    public void selectRoleTest5() throws Exception {
        String token = this.creatTestToken(1L, 1L, 100);
        byte[] responseString = webClient.get().uri("/shops/{shopId}/roles?page=1&pageSize=2", 0)
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.errmsg").isEqualTo("部门id不匹配：0")
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 平台管理员新增角色
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(2)
    public void insertRoleTest1() throws Exception {
        String token = this.creatTestToken(1L, 0L, 100);
        String roleJson = "{\"descr\": \"test\",\"name\": \"test\"}";
        byte[] responseString = webClient.post().uri("/roles")
                .header("authorization", token)
                .bodyValue(roleJson)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\"errno\":0,\"data\":{\"id\":88,\"name\":\"test\",\"createdBy\":1,\"departId\":0,\"desc\":\"test\",\"gmtModified\":null},\"errmsg\":\"成功\"}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 部门管理员新增角色
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(2)
    public void insertRoleTest2() throws Exception {
        String token = this.creatTestToken(2L, 1L, 100);
        String roleJson = "{\"descr\": \"test2\",\"name\": \"test2\"}";
        byte[] responseString = webClient.post().uri("/roles")
                .header("authorization", token)
                .bodyValue(roleJson)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\"errno\":0,\"data\":{\"id\":89,\"name\":\"test2\",\"createdBy\":2,\"departId\":1,\"desc\":\"test2\",\"gmtModified\":null},\"errmsg\":\"成功\"}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 平台管理员新增角色角色名重复
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(2)
    public void insertRoleTest3() throws Exception {
        String token = this.creatTestToken(1L, 0L, 100);
        String roleJson = "{\"descr\": \"管理员test\",\"name\": \"管理员\"}";
        byte[] responseString = webClient.post().uri("/roles")
                .header("authorization", token)
                .bodyValue(roleJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.ROLE_REGISTERED.getCode())
                .jsonPath("$.errmsg").isEqualTo("角色名重复：管理员")
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 平台管理员新增角色角色名为空
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(2)
    public void insertRoleTest4() throws Exception {
        String token = this.creatTestToken(1L, 0L, 100);
        String roleJson = "{\"descr\": \"管理员test\",\"name\": \"\"}";
        byte[] responseString = webClient.post().uri("/roles")
                .header("authorization", token)
                .bodyValue(roleJson)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.errmsg").isEqualTo("角色名不能为空;")
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 未登录新增角色
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(2)
    public void insertRoleTest5() throws Exception {
        String roleJson = "{\"descr\": \"管理员test\",\"name\": \"\"}";
        byte[] responseString = webClient.post().uri("/roles")
                .bodyValue(roleJson)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AUTH_NEED_LOGIN.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.AUTH_NEED_LOGIN.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 伪造token新增角色
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(2)
    public void insertRoleTest6() throws Exception {
        String roleJson = "{\"descr\": \"管理员test\",\"name\": \"\"}";
        byte[] responseString = webClient.post().uri("/roles")
                .header("authorization", "test")
                .bodyValue(roleJson)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AUTH_INVALID_JWT.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.AUTH_INVALID_JWT.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 平台管理员修改角色
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(3)
    public void updateRoleTest1() throws Exception {
        String token = this.creatTestToken(1L, 0L, 100);
        String roleJson = "{\"descr\": \"testU\",\"name\": \"testU\"}";
        byte[] responseString = webClient.put().uri("/shops/{shopId}/roles/{id}", 0, 80)
                .header("authorization", token)
                .bodyValue(roleJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 平台管理员修改角色角色名为空
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(3)
    public void updateRoleTest2() throws Exception {
        String token = this.creatTestToken(1L, 0L, 100);
        String roleJson = "{\"descr\": \"testU\",\"name\": \"\"}";
        byte[] responseString = webClient.put().uri("/shops/{shopId}/roles/{id}", 0, 87)
                .header("authorization", token)
                .bodyValue(roleJson)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.errmsg").isEqualTo("角色名不能为空;")
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 平台管理员修改角色角色名重复
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(3)
    public void updateRoleTest3() throws Exception {
        String token = this.creatTestToken(1L, 0L, 100);
        String roleJson = "{\"descr\": \"客服test\",\"name\": \"客服\"}";
        byte[] responseString = webClient.put().uri("/shops/{shopId}/roles/{id}", 0, 82)
                .header("authorization", token)
                .bodyValue(roleJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.ROLE_REGISTERED.getCode())
                .jsonPath("$.errmsg").isEqualTo("角色名重复：客服")
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 平台管理员修改角色id不存在
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(3)
    public void updateRoleTest4() throws Exception {
        String token = this.creatTestToken(1L, 0L, 100);
        String roleJson = "{\"descr\": \"t\",\"name\": \"t\"}";
        byte[] responseString = webClient.put().uri("/shops/{shopId}/roles/{id}", 0, 0)
                .header("authorization", token)
                .bodyValue(roleJson)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 平台管理员修改角色id与部门id不匹配
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(3)
    public void updateRoleTest5() throws Exception {
        String token = this.creatTestToken(1L, 1L, 100);
        String roleJson = "{\"descr\": \"t\",\"name\": \"t\"}";
        byte[] responseString = webClient.put().uri("/shops/{shopId}/roles/{id}", 0, 85)
                .header("authorization", token)
                .bodyValue(roleJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.errmsg").isEqualTo("部门id不匹配：0")
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 未登录修改角色
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(3)
    public void updateRoleTest6() throws Exception {
        String roleJson = "{\"descr\": \"t\",\"name\": \"t\"}";
        byte[] responseString = webClient.put().uri("/shops/{shopId}/roles/{id}", 0, 85)
                .bodyValue(roleJson)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AUTH_NEED_LOGIN.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.AUTH_NEED_LOGIN.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 伪造token修改角色
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(3)
    public void updateRoleTest7() throws Exception {
        String roleJson = "{\"descr\": \"t\",\"name\": \"t\"}";
        byte[] responseString = webClient.put().uri("/shops/{shopId}/roles/{id}", 0, 85)
                .header("authorization", "test")
                .bodyValue(roleJson)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AUTH_INVALID_JWT.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.AUTH_INVALID_JWT.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 平台管理员删除角色
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(4)
    public void deleteRoleTest1() throws Exception {
        String token = this.creatTestToken(1L, 0L, 100);
        byte[] responseString = webClient.delete().uri("/shops/{shopId}/roles/{id}", 0, 23)
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 管理员删除角色id不存在
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(4)
    public void deleteRoleTest2() throws Exception {
        String token = this.creatTestToken(1L, 0L, 100);
        byte[] responseString = webClient.delete().uri("/shops/{shopId}/roles/{id}", 0, 0)
                .header("authorization", token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .jsonPath("$.errmsg").isEqualTo("角色id不存在：0")
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 管理员删除角色id与部门id不匹配
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(4)
    public void deleteRoleTest3() throws Exception {
        String token = this.creatTestToken(1L, 1L, 100);
        byte[] responseString = webClient.delete().uri("/shops/{shopId}/roles/{id}", 0, 86)
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.errmsg").isEqualTo("部门id不匹配：0")
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 未登录删除角色
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(4)
    public void deleteRoleTest4() throws Exception {
        byte[] responseString = webClient.delete().uri("/shops/{shopId}/roles/{id}", 0, 0)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AUTH_NEED_LOGIN.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.AUTH_NEED_LOGIN.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 伪造token删除角色
     *
     * @author 24320182203281 王纬策
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     */
    @Test
    @Order(4)
    public void deleteRoleTest5() throws Exception {
        byte[] responseString = webClient.delete().uri("/shops/{shopId}/roles/{id}", 0, 0)
                .header("authorization", "test")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AUTH_INVALID_JWT.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.AUTH_INVALID_JWT.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 创建用户token
     *
     * @param userId
     * @param departId
     * @param expireTime
     * @return token
     * createdBy 王纬策 2020/11/30 12:46
     * modifiedBy 王纬策 2020/11/30 12:46
     * @author 24320182203281 王纬策
     */
    private final String creatTestToken(Long userId, Long departId, int expireTime) {
        String token = new JwtHelper().createToken(userId, departId, expireTime);
        log.debug(token);
        return token;
    }
}
