package com.ooad.good.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import com.ooad.good.model.bo.ActivityStatus;
import com.ooad.good.model.bo.Presale;
import com.ooad.good.model.vo.ActivityStatusRetVo;
import com.ooad.good.model.vo.presale.PresaleVo;
import com.ooad.good.service.PresaleService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Api(value="预售服务",tags="presale")
@RestController
@RequestMapping(value = "/goods", produces = "application/json;charset=UTF-8")
public class PresaleController {

    private  static  final Logger logger = LoggerFactory.getLogger(PresaleController.class);

    @Autowired
    private PresaleService presaleService;

    @Autowired
    private HttpServletResponse httpServletResponse;

   
    /**
     *  1
     * 获得预售活动的所有状态
     * @return
     */
    @GetMapping("/presales/states")
    public Object getPresaleState() {
        ActivityStatus[] statuses= ActivityStatus.class.getEnumConstants();
        List<ActivityStatusRetVo> statusVos=new ArrayList<ActivityStatusRetVo>();
        for(int i=0;i<statuses.length;i++){
            statusVos.add(new ActivityStatusRetVo(statuses[i]));
        }
        return ResponseUtil.ok(new ReturnObject<List>(statusVos).getData());
    }

    /**
     * 2
     * 查询所有有效的预售活动
     * @param timeline
     * @param skuId
     * @param shopId
     * @param page
     * @param pagesize
     * @return
     */
    @GetMapping("/presales")
    public Object getAllValidPresales(
            @RequestParam(required = false) Integer timeline,
            @RequestParam(required = false) Long skuId,
            @RequestParam(required = false) Long shopId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pagesize){


        Object returnobject = null;

        if(page <= 0 || pagesize <= 0) {
            returnobject = Common.getNullRetObj(new ReturnObject<>(ResponseCode.FIELD_NOTVALID), httpServletResponse);
        } else {
            ReturnObject<PageInfo<VoObject>> returnObject = presaleService.getAllValidPresales(shopId, skuId, null, timeline, page, pagesize, false);
            returnobject = Common.getPageRetObject(returnObject);
        }

        return returnobject;
    }

    /**
     * 3
     * 管理员查询spu所有预售活动
     * @param shopId
     * @param state
     * @param skuId
     * @param page
     * @param pagesize
     * @return
     */
    @GetMapping("/shops/{shopId}/presales")
    public Object adminQueryPresales(
            @PathVariable Long shopId,
            @RequestParam(required = false) Integer state,
            @RequestParam(required = false) Long skuId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pagesize
    ){

        Object object = null;

        if(page <= 0 || pagesize <= 0) {
            object = Common.getNullRetObj(new ReturnObject<>(ResponseCode.FIELD_NOTVALID), httpServletResponse);
        } else {
            ReturnObject<List<Presale>> returnObject = presaleService.AdminQueryPresales(shopId, skuId, state, null);
            object = Common.decorateReturnObject(returnObject);
        }

        return object;
    }

    /**
     * 4
     *管理员新增sku预售活动
     * @param id
     * @param shopId
     * @param presaleVo
     * @param bindingResult
     * @return
     */
    @PostMapping("/shops/{shopId}/skus/{id}/presales")
    @ResponseBody
    public Object addSkuPresale(@PathVariable(name = "id") Long id,
                                     @PathVariable(name="shopId") Long shopId,
                                     @Validated @NotNull @RequestBody PresaleVo presaleVo,
                                     BindingResult bindingResult){

        //校验前端数据
        Object retObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != retObject) {
            logger.debug("validate fail");
            return retObject;
        }

        //不能为空
        if(presaleVo.getEndTime()==null || presaleVo.getBeginTime()==null ||presaleVo.getPayTime() == null){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        //endtime<now,begin<now
        if(presaleVo.getEndTime().isBefore(LocalDateTime.now())||presaleVo.getBeginTime().isBefore(LocalDateTime.now())){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        //begintime>endtime
        if(presaleVo.getEndTime().isBefore(LocalDateTime.now())||
                presaleVo.getEndTime().isBefore(presaleVo.getPayTime())||
                presaleVo.getPayTime().isBefore(presaleVo.getBeginTime())){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }


        ReturnObject returnObject = null;

        returnObject = presaleService.addSkuPresale(shopId,id,presaleVo);

        if (returnObject.getCode() == ResponseCode.OK) {
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }
    }

    /**
     * 5
     * 管理员修改sku预售活动
     * @param shopId
     * @param departId
     * @param id
     * @param presaleVo
     * @param bindingResult
     * @return
     */
    @Audit
    @PutMapping("/shops/{shopId}/presales/{id}")
    public Object modifyPresaleofSKU(@PathVariable Long shopId, @Depart Long departId, @PathVariable Long id, @Validated @NotNull @RequestBody(required = true) PresaleVo presaleVo, BindingResult bindingResult){

        //校验前端数据
        Object retObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != retObject) {
            logger.debug("validate fail");
            return retObject;
        }

        //paytime<begintime
        if(presaleVo.getBeginTime()!=null &&
                presaleVo.getPayTime() != null &&
                presaleVo.getPayTime().isBefore(presaleVo.getBeginTime())){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }

        //paytime>endtime
        if(presaleVo.getEndTime()!=null && presaleVo.getPayTime() != null && presaleVo.getPayTime().isAfter(presaleVo.getEndTime())){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }

        //begintime>endtime
        if(presaleVo.getBeginTime()!=null && presaleVo.getEndTime() != null && presaleVo.getBeginTime().isAfter(presaleVo.getEndTime())){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }

        //endtime<now
        if(presaleVo.getEndTime()!=null && presaleVo.getEndTime().isBefore(LocalDateTime.now())){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        //begintime<now
        if(presaleVo.getBeginTime()!=null && presaleVo.getBeginTime().isBefore(LocalDateTime.now())){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        //paytime<now
        if(presaleVo.getPayTime()!=null && presaleVo.getPayTime().isBefore(LocalDateTime.now())){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }

        if(shopId!=departId && departId!=0L)
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE));

        ReturnObject returnObject = presaleService.modifyPresaleOfSKU(shopId,id,presaleVo);
        if (returnObject.getCode() == ResponseCode.OK) {
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }
    }


    /**
     * 6
     * 管理员逻辑删除sku预售活动
     * @param shopId
     * @param departId
     * @param id
     * @return
     */
    @Audit
    @DeleteMapping("/shops/{shopId}/presales/{id}")
    public Object cancelPresaleOfSKU(@PathVariable Long shopId, @Depart Long departId, @PathVariable Long id) {
        if(shopId!=departId && departId!=0L)
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE));
        ReturnObject returnObject =  presaleService.cancelPresaleOfSKU(shopId, id);
        if (returnObject.getCode() == ResponseCode.OK) {
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }
    }


    /**
     * 7
     * 管理员上架sku预售活动
     * @param id
     * @param departId
     * @param shopId
     * @return
     */
    @Audit
    @ResponseBody
    @PutMapping("/shops/{shopId}/presales/{id}/onshelves")
    public Object putPresaleOnShelves(@PathVariable Long id, @Depart Long departId, @PathVariable Long shopId){
        if(shopId!=departId && departId!=0L)
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE));
        ReturnObject returnObject = presaleService.putPresaleOnShelves(shopId,id);
        if (returnObject.getCode() == ResponseCode.OK) {
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }
    }

    /**
     * 8
     * 管理员下架sku优惠活动
     * @param id
     * @param departId
     * @param shopId
     * @return
     */
    @Audit
    @ResponseBody
    @PutMapping("/shops/{shopId}/presales/{id}/offshelves")
    public Object putPresaleOffShelves(@PathVariable Long id, @Depart Long departId, @PathVariable Long shopId){

        if(shopId!=departId && departId!=0L)
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE));

        ReturnObject returnObject = presaleService.putPresaleOffShelves(shopId,id);
        if (returnObject.getCode() == ResponseCode.OK) {
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }
    }
 
}
