package com.ooad.good.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.ooad.good.model.bo.FlashSale;
import com.ooad.good.model.bo.FlashSaleItem;
import com.ooad.good.model.bo.Sku;
import com.ooad.good.model.vo.FlashSaleVo;
import com.ooad.good.model.vo.FlashSkuVo;
import com.ooad.good.service.FlashSaleService;
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
 * @AuthorId: 24320182203185
 * @Author: Chaoyang Deng
 * @Date: 2020/12/15 下午9:01
 */
@Api(value = "秒杀服务", tags = "goods")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/goods", produces = "application/json;charset=UTF-8")
public class FlashSaleController {
    private  static  final Logger logger = LoggerFactory.getLogger(FlashSaleController.class);
    @Autowired
    private HttpServletResponse httpServletResponse;
    @Autowired
    FlashSaleService flashsaleService;

    /**
     * 查询某一时段秒杀活动详情
     * @param id
     * @return
     */
    @ApiOperation(value = "某一时间段秒杀活动详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("/timesegments/{id}/flashsales")
    public Object getFlashSales( @Validated @PathVariable Long id){
        logger.debug("getFlashSalesById");
        ReturnObject<List> returnObject =  flashsaleService.getAllFlashsale((long) id);
        if (returnObject.getCode() == ResponseCode.OK) {
            //logger.info(returnObject.getData().toString());
            return Common.getListRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }
    }
     /**
      * 平台管理员在某个时间段下新建秒杀
      * @param vo
      * @param bindingResult
      * @param did
      * @param seqid
      * @return
      */
    @ApiOperation(value = "新增秒杀活动", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "FlashSaleVo", name = "vo", value = "秒杀活动信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PostMapping("shops/{did}/timesegments/{id}/flashsales")
    public Object insertFlashSale(@Validated @RequestBody FlashSaleVo vo, BindingResult bindingResult,
                                  @Validated @PathVariable Long did,
                             @Validated @PathVariable Long seqid,
                             @LoginUser @ApiIgnore @RequestParam(required = false) Long userId,
                             @Depart @ApiIgnore @RequestParam(required = false) Long departId) {
        logger.debug("insert flashsale by userId:" + userId);
        //校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            logger.debug("validate fail");
            return returnObject;
        }
        FlashSale flashSale = vo.createFlashSale();
        flashSale.setGmtCreate(LocalDateTime.now());
        flashSale.setGmtModified(LocalDateTime.now());
        flashSale.setTimeSeq(seqid);
        Byte b=0;
        flashSale.getState().setCode(b);//处于下线状态

        ReturnObject retObject = flashsaleService.insertFlashsale(flashSale);
        if (retObject.getData() != null) {
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.getRetObject(retObject);
        } else {
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }
    }
    /**
     * 查询当前时段秒杀列表
     * @return
     */
    @ApiOperation(value = "查询当前时段秒杀列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("/flashsales/current")
    public Object getCurentFlashSales(){

        logger.debug("getCurrentFlashSales");
        ReturnObject<List> returnObject =flashsaleService.getCurrentFlashsale();
        if (returnObject.getCode() == ResponseCode.OK) {
            //logger.info(returnObject.getData().toString());
            return Common.getListRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }
    }
    /**
     * 向秒杀活动添加商品
     * @param did
     * @param id
     * @param vo
     * @param bindingResult
     * @return
     */

    @ApiOperation(value = "向秒杀活动添加商品", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "FlashSkuVo", name = "vo", value = "Sku", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PostMapping("/shops/{did}/flashsales/{id}/flashitems")
    public Object insertSkuToFlash(@Validated @RequestBody FlashSkuVo vo, BindingResult bindingResult,
                             @Validated @PathVariable Long did,
                             @Validated @PathVariable Long id,
                             @LoginUser @ApiIgnore @RequestParam(required = false) Long userId,
                             @Depart @ApiIgnore @RequestParam(required = false) Long departId) {
        logger.debug("insert sku by userId:" + userId);
        //校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            logger.debug("validate fail");
            return returnObject;
        }
        FlashSaleItem flashSaleItem=null;
        flashSaleItem.setGmtModified(LocalDateTime.now());
        flashSaleItem.setGmtCreate(LocalDateTime.now());
        Sku sku=new Sku();
        sku.setId(vo.getSkuId());
        flashSaleItem.setSaleId(id);
        flashSaleItem.setPrice(vo.getPrice());
        flashSaleItem.setQuantity(vo.getQuantity());
        ReturnObject retObject = flashsaleService.insertSkuToFlash(flashSaleItem);

        if (retObject.getData() != null) {
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.getRetObject(retObject);
        } else {
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }
    }
    /**
     * 平台管理员在秒杀活动删除商品SKU
     * @param did
     * @param fid
     * @param id
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "管理员在秒杀活动删除商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit // 需要认证
    @DeleteMapping("/shops/{did}/flashsales/{fid}/flashitems/{id}")
    public Object deleteFlashItem(@PathVariable("did")Long did,
                             @PathVariable("fid")Long fid,
                             @PathVariable("id")Long id,
                             BindingResult bindingResult){
        logger.debug("delete flash sale item: id =" +id);
        if (logger.isDebugEnabled()) {
            logger.debug("deleteFlashSaleItemInfo: id = "+ id );
        }
        // 校验前端数据
        Object retObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (retObject != null) {
            logger.info("incorrect data received while deleteFlashSaleItemInfo id = " + id);
            return retObject;
        }

        ReturnObject returnObject=flashsaleService.deleteFlashsaleItem(fid,id);
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * 管理员上线秒杀活动
     * @param did
     * @param id
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "管理员上线秒杀活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit // 需要认证
    @PutMapping("/shops/{did}/flashsales/{id}/onshelves")
    public Object onshelvesflashSale(@PathVariable("did")Long did,@PathVariable("id")Long id,BindingResult bindingResult){
        logger.debug("onshelves flashsale: id =" +id);
        // 校验前端数据
        Object retObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (retObject != null) {
            logger.info("incorrect data received while onshelf flashsale Info id = " + id);
            return retObject;
        }
        ReturnObject returnObject=flashsaleService.onshelvesflashSale(id);
        return Common.decorateReturnObject(returnObject);
    }
    /**
     * 管理员下线秒杀活动
     * @param did
     * @param id
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "管理员下线秒杀活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit // 需要认证
    @PutMapping("/shops/{did}/flashsales/{id}/offshelves")
    public Object offshelvesflashSale(@PathVariable("did")Long did,@PathVariable("id")Long id,BindingResult bindingResult){
        logger.debug("offshelves flashsale: id =" +id);
        // 校验前端数据
        Object retObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (retObject != null) {
            logger.info("incorrect data received while offshelf flashsale Info id = " + id);
            return retObject;
        }
        ReturnObject returnObject=flashsaleService.offshelvesflashSale(id);
        return Common.decorateReturnObject(returnObject);
    }
    /**
     * 管理员修改秒杀活动
     * @param did
     * @param id
     * @param vo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "管理员修改秒杀活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit // 需要认证
    @PutMapping("/shops/{did}/flashsales/{id}/offshelves")
    public Object modifyflashSale(@PathVariable("did")Long did,@PathVariable("id")Long id,
                                  @Validated @RequestBody FlashSaleVo vo,
                                  BindingResult bindingResult){
        logger.debug("modify flashsale: id =" +id);
        // 校验前端数据
        Object retObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (retObject != null) {
            logger.info("incorrect data received while offshelf flashsale Info id = " + id);
            return retObject;
        }
        FlashSale flashSale = vo.createFlashSale();
        flashSale.setId(id);
        flashSale.setGmtModified(LocalDateTime.now());

        ReturnObject returnObject = flashsaleService.modifyFlashsale(flashSale);
        if (returnObject.getData() != null) {
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.getRetObject(returnObject);
        } else {
            return Common.getNullRetObj(new ReturnObject<>(returnObject.getCode(), returnObject.getErrmsg()), httpServletResponse);
        }
    }
    /**
     * 管理员删除秒杀活动
     * @param did
     * @param id
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "管理员下线秒杀活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit // 需要认证
    @DeleteMapping("/shops/{did}/flashsales/{id}")
    public Object deleteflashSale(@PathVariable("did")Long did,@PathVariable("id")Long id,BindingResult bindingResult){
        logger.debug("delete flashsale: id =" +id);
        // 校验前端数据
        Object retObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (retObject != null) {
            logger.info("incorrect data received while delete flashsale Info id = " + id);
            return retObject;
        }
        ReturnObject returnObject=flashsaleService.deleteflashSale(id);
        return Common.decorateReturnObject(returnObject);
    }
}
