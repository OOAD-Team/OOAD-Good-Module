package com.ooad.good.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.ooad.good.model.bo.Shop;
import com.ooad.good.model.vo.ShopConVo;
import com.ooad.good.model.vo.ShopVo;
import com.ooad.good.service.ShopService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: Chaoyang Deng
 * @Date: 2020/12/9 上午8:30
 */

@Api(value = "商品服务", tags = "goods")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/goods", produces = "application/json;charset=UTF-8")
public class ShopController {

    private  static  final Logger logger = LoggerFactory.getLogger(ShopController.class);
    @Autowired
    private HttpServletResponse httpServletResponse;
    @Autowired
    ShopService shopService;


    /**
     * 获得店铺所有状态
     * @return
     */
    @ApiOperation(value = "获得店铺所有状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("/shops/states")
    public Object getAllShopStates(){

        logger.debug("getAllShops");
        ReturnObject<List> returnObject =  shopService.getAllShopStates();
        if (returnObject.getCode() == ResponseCode.OK) {
            //logger.info(returnObject.getData().toString());
            return Common.getListRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }
    }

    /**
     * 新增商铺
     * @param vo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "新增店铺", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "ShopVo", name = "vo", value = "可修改的店铺信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 920, message = "名字已被注册"),
    })
    @Audit
    @PostMapping("/shops")
    public Object insertShop(@Validated @RequestBody ShopVo vo, BindingResult bindingResult,
                             @LoginUser @ApiIgnore @RequestParam(required = false) Long userId,
                             @Depart @ApiIgnore @RequestParam(required = false) Long departId) {
        logger.debug("insert role by userId:" + userId);
        //校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            logger.debug("validate fail");
            return returnObject;
        }
        Shop shop = vo.createShop();
        Byte state=0;//默认0为未审核状态，1为未上线状态，2为上线状态,3为审核未通过状态，4为逻辑删除
        shop.setGmtCreate(LocalDateTime.now());
        shop.setState(state);
        ReturnObject retObject = shopService.insertShop(shop);
        if (retObject.getData() != null) {
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.getRetObject(retObject);
        } else {
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }
    }
    /**
     * 店家修改店铺信息
     * @param id
     * @param vo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "修改任意店铺信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 920, message = "名字已被注册"),
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit // 需要认证
    @PutMapping("/shops/{id}")
    public Object modifyShopInfo(@PathVariable Long id, @Validated @RequestBody ShopVo vo, BindingResult bindingResult) {
        if (logger.isDebugEnabled()) {
            logger.debug("modifyShopInfo: id = "+ id +" vo = " + vo);
        }
        // 校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (returnObject != null) {
            logger.info("incorrect data received while modifyShopInfo id = " + id);
            return returnObject;
        }
        ReturnObject returnObj = shopService.modifyShopInfo(id, vo);
        return Common.decorateReturnObject(returnObj);
    }

    /**
     * 管理员或店家关闭店铺
     * @param id
     * @return
     */
    @ApiOperation(value = "删除任意店铺")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit // 需要认证
    @DeleteMapping("/shops/{id}")
    public Object deleteShop(@PathVariable("id")Long id,BindingResult bindingResult){
        logger.debug("delete shop: id =" +id);
        if (logger.isDebugEnabled()) {
            logger.debug("modifyShopInfo: id = "+ id );
        }
        // 校验前端数据
        Object retObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (retObject != null) {
            logger.info("incorrect data received while modifyShopInfo id = " + id);
            return retObject;
        }

        ReturnObject returnObject=shopService.deleteShop(id);
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * 平台管理员审核店铺信息
     * @param id
     * @param newid
     * @param vo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "管理员审核店铺")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit // 需要认证
    @PutMapping("/shops/{shopId}/newshops/{id}/audit")
    public Object auditShopInfo(@PathVariable Long id, @PathVariable Long newid, @Validated @RequestBody ShopConVo vo, BindingResult bindingResult)
    {
        if (logger.isDebugEnabled()) {
            logger.debug("modifyShopInfo: id = "+ id +" vo = " + vo);
        }
        // 校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (returnObject != null) {
            logger.info("incorrect data received while modifyShopInfo id = " + id);
            return returnObject;
        }
            return shopService.auditShopInfo(newid,vo.isConclusion());
    }

    /**
     * 管理员上线店铺
     * @param id
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "管理员上线店铺")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit // 需要认证
    @PutMapping("/shops/{id}/onshelves")
    public Object onshelvesShop(@PathVariable("id")Long id,BindingResult bindingResult){
        logger.debug("onshelves shop: id =" +id);
        if (logger.isDebugEnabled()) {
            logger.debug("modifyShopInfo: id = "+ id );
        }
        // 校验前端数据
        Object retObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (retObject != null) {
            logger.info("incorrect data received while modifyShopInfo id = " + id);
            return retObject;
        }
        ReturnObject returnObject=shopService.onshelvesShop(id);
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * 管理员下线店铺
     * @param id
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "管理员下线店铺")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit // 需要认证
    @PutMapping("/shops/{id}/offshelves")
    public Object offshelvesShop(@PathVariable("id")Long id,BindingResult bindingResult){
        logger.debug("offshelves shop: id =" +id);
        if (logger.isDebugEnabled()) {
            logger.debug("modifyShopInfo: id = "+ id );
        }
        // 校验前端数据
        Object retObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (retObject != null) {
            logger.info("incorrect data received while modifyShopInfo id = " + id);
            return retObject;
        }
        ReturnObject returnObject=shopService.offshelvesShop(id);
        return Common.decorateReturnObject(returnObject);
    }
}
