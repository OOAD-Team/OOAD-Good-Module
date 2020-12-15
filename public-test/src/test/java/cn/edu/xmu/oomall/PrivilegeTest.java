package cn.edu.xmu.oomall;

import cn.edu.xmu.ooad.Application;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Ming Qiu
 * @date Created in 2020/11/4 0:33
 **/
@SpringBootTest(classes = Application.class)   //标识本类是一个SpringBootTest
public class PrivilegeTest {

    private WebTestClient webClient;

    public PrivilegeTest(){
        this.webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
    }

    /**
     * @author Song Runhan
     * @date Created in 2020/11/4 16:00
     */
    @Test
    public void login1() throws Exception {
        String requireJson = null;
        byte[] responseString = null;

        WebTestClient.RequestHeadersSpec res = null;

        //region Email未确认用户登录
        requireJson = "{\"userName\":\"5264500009\",\"password\":\"123456\"}";
        res = webClient.post().uri("/privileges/login").bodyValue(requireJson);
        responseString = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.EMAIL_NOTVERIFIED.getCode())
                .returnResult()
                .getResponseBodyContent();
        //endregion
    }

    /**
     * @author Song Runhan
     * @date Created in 2020/11/4 16:00
     */
    @Test
    public void login2() throws Exception {
        String requireJson = null;
        byte[] responseString = null;
        WebTestClient.RequestHeadersSpec res = null;

        //region 密码错误的用户登录
        requireJson = "{\"userName\":\"13088admin\",\"password\":\"000000\"}";
        res = webClient.post().uri("/privileges/login").bodyValue(requireJson);
        responseString = res.exchange()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AUTH_INVALID_ACCOUNT.getCode())
                .jsonPath("$.data").doesNotExist()
                .returnResult().getResponseBodyContent();
        //endregion
    }

    /**
     * @author Song Runhan
     * @date Created in 2020/11/4 16:00
     */
    @Test
    public void login3() throws Exception {
        String requireJson = null;
        byte[] responseString = null;

        WebTestClient.RequestHeadersSpec res = null;

        //region 用户名错误的用户登录
        requireJson = "{\"userName\":\"NotExist\",\"password\":\"123456\"}";
        res = webClient.post().uri("/privileges/login").bodyValue(requireJson);
        responseString = res.exchange().expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AUTH_INVALID_ACCOUNT.getCode())
                .jsonPath("$.data").doesNotExist()
                .returnResult().getResponseBodyContent();
        //endregion
    }

    /**
     * @author Song Runhan
     * @date Created in 2020/11/4 16:00
     */
    @Test
    public void login4() throws Exception {
        String requireJson = null;
        byte[] responseString = null;
        WebTestClient.RequestHeadersSpec res = null;

        //region 没有输入用户名的用户登录
        requireJson = "{\"password\":\"123456\"}";
        res = webClient.post().uri("/privileges/login").bodyValue(requireJson);
        responseString = res.exchange().expectStatus().isBadRequest()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.errmsg").isEqualTo("必须输入用户名;")
                .returnResult().getResponseBodyContent();
        //endregion
    }

    /**
     * @author Song Runhan
     * @date Created in 2020/11/4 16:00
     */
    @Test
    public void login5() throws Exception {
        String requireJson = null;
        byte[] responseString = null;
        WebTestClient.RequestHeadersSpec res = null;

        //region 没有输入密码（密码空）的用户登录
        requireJson = "{\"userName\":\"537300010\",\"password\":\"\"}";
        res = webClient.post().uri("/privileges/login").bodyValue(requireJson);
        responseString = res.exchange().expectStatus().isBadRequest()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.errmsg").isEqualTo("必须输入密码;")
                .returnResult().getResponseBodyContent();
        //endregion
    }

    /**
     * @author Song Runhan
     * @date Created in 2020/11/4 16:00
     */
    @Test
    public void login6() throws Exception {
        String requireJson = null;
        byte[] response = null;
        WebTestClient.RequestHeadersSpec res = null;

        //region 用户重复登录
        requireJson = "{\"userName\":\"13088admin\",\"password\":\"123456\"}";
        res = webClient.post().uri("/privileges/login").bodyValue(requireJson);
        response = res.exchange().expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").exists()
                .returnResult().getResponseBodyContent();

        res = webClient.post().uri("/privileges/login").bodyValue(requireJson);

        byte[] response1 = res.exchange().expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult().getResponseBodyContent();

        String jwt = JacksonUtil.parseString(new String(response,"UTF-8"), "data");
        String jwt1 = JacksonUtil.parseString(new String(response1,"UTF-8"), "data");
        assertNotEquals(jwt, jwt1);
    }

    /**
     * @author Song Runhan
     * @date Created in 2020/11/4 16:00
     */
    @Test
    public void login7() throws Exception {
        String requireJson = null;
        byte[] responseString = null;
        WebTestClient.RequestHeadersSpec res = null;
        //region 当前状态不可登录的用户登录
        requireJson = "{\"userName\":\"8884810086\",\"password\":\"123456\"}";
        res = webClient.post().uri("/privileges/login").bodyValue(requireJson);

        responseString = res.exchange().expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AUTH_USER_FORBIDDEN.getCode())
                .jsonPath("$.data").doesNotExist()
                .returnResult().getResponseBodyContent();
        //endregion
    }

    @Test
    public void login8() throws Exception {
        String requireJson = null;

        //正常用户登录
        requireJson = "{\"userName\":\"13088admin\",\"password\":\"123456\"}";
        byte[] ret = webClient.post().uri("/privileges/login").bodyValue(requireJson).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        //endregion
    }

    /**
     * 获取所有权限（第一页）
     * @throws Exception
     */
    @Test
    public void getAllPriv1() throws Exception{
        String token = this.login("13088admin", "123456");
        byte[] responseString = webClient.get().uri("/privileges").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBodyContent();

        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":18,\"pages\":2,\"pageSize\":10,\"page\":1,\"list\":[{\"id\":2,\"name\":\"查看任意用户信息\",\"url\":\"/adminusers/{id}\",\"requestType\":0,\"gmtCreate\":\"2020-11-01T09:52:20\",\"gmtModified\":\"2020-11-02T21:51:45\"},{\"id\":3,\"name\":\"修改任意用户信息\",\"url\":\"/adminusers/{id}\",\"requestType\":2,\"gmtCreate\":\"2020-11-01T09:53:03\",\"gmtModified\":\"2020-11-02T21:51:45\"},{\"id\":4,\"name\":\"删除用户\",\"url\":\"/adminusers/{id}\",\"requestType\":3,\"gmtCreate\":\"2020-11-01T09:53:36\",\"gmtModified\":\"2020-11-02T21:51:45\"},{\"id\":5,\"name\":\"恢复用户\",\"url\":\"/adminusers/{id}/release\",\"requestType\":2,\"gmtCreate\":\"2020-11-01T09:59:24\",\"gmtModified\":\"2020-11-02T21:51:45\"},{\"id\":6,\"name\":\"禁止用户登录\",\"url\":\"/adminusers/{id}/forbid\",\"requestType\":2,\"gmtCreate\":\"2020-11-01T10:02:32\",\"gmtModified\":\"2020-11-02T21:51:45\"},{\"id\":7,\"name\":\"赋予用户角色\",\"url\":\"/adminusers/{id}/roles/{id}\",\"requestType\":1,\"gmtCreate\":\"2020-11-01T10:02:35\",\"gmtModified\":\"2020-11-02T21:51:45\"},{\"id\":8,\"name\":\"取消用户角色\",\"url\":\"/adminusers/{id}/roles/{id}\",\"requestType\":3,\"gmtCreate\":\"2020-11-01T10:03:16\",\"gmtModified\":\"2020-11-02T21:51:45\"},{\"id\":9,\"name\":\"新增角色\",\"url\":\"/roles\",\"requestType\":1,\"gmtCreate\":\"2020-11-01T10:04:09\",\"gmtModified\":\"2020-11-02T21:51:45\"},{\"id\":10,\"name\":\"删除角色\",\"url\":\"/roles/{id}\",\"requestType\":3,\"gmtCreate\":\"2020-11-01T10:04:42\",\"gmtModified\":\"2020-11-02T21:51:45\"},{\"id\":11,\"name\":\"修改角色信息\",\"url\":\"/roles/{id}\",\"requestType\":2,\"gmtCreate\":\"2020-11-01T10:05:20\",\"gmtModified\":\"2020-11-02T21:51:45\"}]},\"errmsg\":\"成功\"}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, "UTF-8"), true);
    }

    @Test
    public void getAllPriv2() throws Exception {
        webClient.get().uri("/privileges")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectHeader().contentType("application/json;charset=UTF-8");

    }

    /**
     * 获取所有权限（第二页）
     * @throws Exception
     */
    @Test
    public  void getAllPriv3() throws Exception {
        String token = this.login("13088admin", "123456");
        // 我只能把query加在路径后面了，找不到一个加query的函数
        byte[] responseString = webClient.get().uri("/privileges?page=2&pageSize=10").header("authorization", token).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data.pageSize").isEqualTo(10)
                .jsonPath("$.data.list.size() ").isEqualTo(8)
                .returnResult().getResponseBodyContent();

        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":18,\"pages\":2,\"pageSize\":10,\"page\":2,\"list\":[{\"id\":12,\"name\":\"给角色增加权限\",\"url\":\"/roles/{id}/privileges/{id}\",\"requestType\":1,\"gmtCreate\":\"2020-11-01T10:06:03\",\"gmtModified\":\"2020-11-02T21:51:46\"},{\"id\":13,\"name\":\"取消角色权限\",\"url\":\"/roleprivileges/{id}\",\"requestType\":3,\"gmtCreate\":\"2020-11-01T10:06:43\",\"gmtModified\":\"2020-11-03T21:30:31\"},{\"id\":14,\"name\":\"修改权限信息\",\"url\":\"/privileges/{id}\",\"requestType\":2,\"gmtCreate\":\"2020-11-01T10:08:18\",\"gmtModified\":\"2020-11-02T21:51:46\"},{\"id\":15,\"name\":\"查看所有用户的角色\",\"url\":\"/adminusers/{id}/roles\",\"requestType\":0,\"gmtCreate\":\"2020-11-03T17:53:38\",\"gmtModified\":\"2020-11-03T19:48:47\"},{\"id\":16,\"name\":\"查看所有代理\",\"url\":\"/proxies\",\"requestType\":0,\"gmtCreate\":\"2020-11-03T17:55:31\",\"gmtModified\":\"2020-11-03T19:48:47\"},{\"id\":17,\"name\":\"禁止代理关系\",\"url\":\"/allproxies/{id}\",\"requestType\":3,\"gmtCreate\":\"2020-11-03T17:57:45\",\"gmtModified\":\"2020-11-03T19:48:47\"},{\"id\":18,\"name\":\"取消任意用户角色\",\"url\":\"/adminuserroles/{id}\",\"requestType\":3,\"gmtCreate\":\"2020-11-03T19:52:04\",\"gmtModified\":\"2020-11-03T19:56:43\"},{\"id\":19,\"name\":\"管理员设置用户代理关系\",\"url\":\"/ausers/{id}/busers/{id}:\",\"requestType\":1,\"gmtCreate\":\"2020-11-04T13:10:02\",\"gmtModified\":null}]},\"errmsg\":\"成功\"}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, "UTF-8"), true);
    }

    /**
     * @author Song Runhan
     * @date Created in 2020/11/4/ 16:00
     */
    @Test
    public void logout() throws  Exception{
        String requireJson = null;
        byte[] responseString = null;
        WebTestClient.RequestHeadersSpec res = null;

        requireJson = "{\"userName\":\"13088admin\",\"password\":\"123456\"}";
        res = webClient.post().uri("/privileges/login").bodyValue(requireJson);
        responseString = res.exchange().expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").exists()
                .returnResult().getResponseBodyContent();
        String json = JacksonUtil.parseString(new String(responseString, "UTF-8"),"data");

        //region 用户正常登出
        res = webClient.get().uri("/privileges/logout").header("authorization",json);
        responseString = res.exchange().expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult().getResponseBodyContent();
        //endregion
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
