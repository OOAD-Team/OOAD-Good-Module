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
import com.ooad.good.model.vo.ActivityStatusRetVo;
import com.ooad.good.model.vo.groupon.GrouponVo;
import com.ooad.good.service.GrouponService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@Api(value = "团购服务", tags = "Groupon")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/goods", produces = "application/json;charset=UTF-8")
//@RequestMapping(produces = "application/json;charset=UTF-8")
public class GrouponController {

    @Autowired
    GrouponService grouponService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    private static final Logger logger = LoggerFactory.getLogger(GrouponController.class);


    /**
     * 获得团购活动的所有状态
     * * @return
     */
    @GetMapping("/groupons/states")
    public Object getgrouponState() {
        ActivityStatus[] states = ActivityStatus.class.getEnumConstants();
        List<ActivityStatusRetVo> stateVos = new ArrayList<ActivityStatusRetVo>();
        for (int i = 0; i < states.length; i++) {
            stateVos.add(new ActivityStatusRetVo(states[i]));
        }
        return ResponseUtil.ok(new ReturnObject<List>(stateVos).getData());
    }


    /**
     * 查询所有团购活动
     *
     * @param timeline
     * @param spu_id
     * @param shopId
     * @param page
     * @param pagesize
     * @return
     */
    @ResponseBody
    @GetMapping("/groupons")
    public Object customerQueryGroupons(
            @RequestParam(required = false) Integer timeline,
            @RequestParam(required = false) Long spu_id,
            @RequestParam(required = false) Long shopId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pagesize) {


        Object object = null;

        if (page <= 0 || pagesize <= 0) {
            object = Common.getNullRetObj(new ReturnObject<>(ResponseCode.FIELD_NOTVALID), httpServletResponse);
        } else {
            ReturnObject<PageInfo<VoObject>> returnObject = grouponService.queryGroupons(shopId, spu_id, null, timeline, null, null, page, pagesize, false);
            object = Common.getPageRetObject(returnObject);
        }

        return object;
    }


    /**
     * 管理员查询所有团购
     *
     * @param id
     * @param state
     * @param spuid
     * @param beginTime
     * @param endTime
     * @param page
     * @param pagesize
     * @return
     */
    @Audit
    @ResponseBody
    @GetMapping("/shops/{id}/groupons")
    public Object adminQueryGroupons(
            @PathVariable Long id,
            @RequestParam(required = false) Integer state,
            @RequestParam(required = false) Long spuid,
            @RequestParam(required = false) String beginTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pagesize
    ) {

        Object object = null;
        if (page <= 0 || pagesize <= 0) {
            object = Common.getNullRetObj(new ReturnObject<>(ResponseCode.FIELD_NOTVALID), httpServletResponse);
        } else {
            //时间处理

            LocalDateTime bt = null;
            if (beginTime != null) {
                try {
                    bt = LocalDateTime.parse(beginTime, DateTimeFormatter.ISO_DATE_TIME);
                } catch (Exception e) {
                    object = Common.decorateReturnObject(new ReturnObject<>(ResponseCode.FIELD_NOTVALID));
                }
            }

            LocalDateTime et = null;
            if (endTime != null) {
                try {
                    et = LocalDateTime.parse(endTime, DateTimeFormatter.ISO_DATE_TIME);
                } catch (Exception e) {
                    object = Common.decorateReturnObject(new ReturnObject<>(ResponseCode.FIELD_NOTVALID));
                }
            }

            ReturnObject<PageInfo<VoObject>> returnObject = grouponService.queryGroupons(id, spuid, state, null, bt, et, page, pagesize, true);
            object = Common.getPageRetObject(returnObject);
        }

        return object;
    }


    /**
     * 管理员对spu新增团购活动
     *
     * @param id
     * @param shopId
     * @param grouponVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "管理员对SPU新增团购活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "用户token", paramType = "header", dataType = "String", required = true),
            @ApiImplicitParam(name = "shopId", value = "商铺id", paramType = "path", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "id", value = "商品SPUid", paramType = "path", dataType = "Integer", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @PostMapping("/shops/{shopId}/spus/{id}/groupons")
    @ResponseBody
    public Object createGrouponofSPU(@PathVariable(name = "id") Long id, @PathVariable(name = "shopId") Long shopId, @Validated @RequestBody(required = true) GrouponVo grouponVo, BindingResult bindingResult) {

        Object retObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != retObject) {
            logger.debug("validate fail");
            return retObject;
        }

        //beginTime，endTime不能空
        if (grouponVo.getBeginTime() == null || grouponVo.getEndTime() == null) {
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        //endtime<now,begintime>endtime, begintime<now
        if (grouponVo.getEndTime().isBefore(LocalDateTime.now()) ||
                grouponVo.getBeginTime().isAfter(grouponVo.getEndTime()) ||
                grouponVo.getBeginTime().isBefore(LocalDateTime.now())) {
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }

        ReturnObject returnObject = grouponService.createGrouponofSPU(shopId, id, grouponVo);
        if (returnObject.getCode() == ResponseCode.OK) {
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }
    }


    /**
     * 管理员修改spu团购活动
     *
     * @param shopId
     * @param departId
     * @param id
     * @param grouponVo
     * @param bindingResult
     * @return
     */
    @Audit
    @ResponseBody
    @PutMapping("/shops/{shopId}/groupons/{id}")
    public Object modifyGrouponofSPU(@PathVariable Long shopId, @Depart Long departId, @PathVariable Long id, @Validated @RequestBody(required = true) GrouponVo grouponVo, BindingResult bindingResult) {

        Object ret = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != ret) {
            logger.debug("validate fail");
            return ret;
        }

        //begintime<now
        if ((grouponVo.getBeginTime() != null) && (grouponVo.getBeginTime().isBefore(LocalDateTime.now())))
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        //endtime<now
        if ((grouponVo.getEndTime() != null) && (grouponVo.getEndTime().isBefore(LocalDateTime.now())))
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        //begintime>endtime
        if (grouponVo.getBeginTime() != null
                && grouponVo.getEndTime() != null
                && grouponVo.getEndTime().isBefore(grouponVo.getBeginTime()))
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));

        if (shopId != departId && departId != 0L)
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE));
        Object retObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != retObject) {
            logger.debug("validate fail");
            return retObject;
        }

        //BeginTime，EndTime的验证
        if (grouponVo.getEndTime() != null && grouponVo.getEndTime().isBefore(LocalDateTime.now())) {
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        if (grouponVo.getEndTime() != null && grouponVo.getBeginTime() != null && grouponVo.getEndTime().isBefore(grouponVo.getBeginTime())) {
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }

        ReturnObject returnObject = grouponService.modifyGrouponofSPU(shopId, id, grouponVo);
        if (returnObject.getCode() == ResponseCode.OK) {
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }
    }


    /**
     * 管理员删除spu团购活动
     *
     * @param shopId
     * @param departId
     * @param id
     * @return
     */
    @Audit
    @DeleteMapping("/shops/{shopId}/groupons/{id}")
    public Object cancelGrouponofSPU(@PathVariable Long shopId, @Depart Long departId, @PathVariable Long id) {

        if (shopId != departId && departId != 0L)
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE));

        ReturnObject returnObject = grouponService.cancelGrouponofSPU(shopId, id);
        if (returnObject.getCode() == ResponseCode.OK) {
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }
    }


    /**
     * 管理员上线spu团购活动
     *
     * @param id
     * @param departId
     * @param shopId
     * @return
     */
    @Audit
    @ResponseBody
    @PutMapping("/shops/{shopId}/groupons/{id}/onshelves")
    public Object putGrouponOnShelves(@PathVariable Long id, @Depart Long departId, @PathVariable Long shopId) {
        if (shopId != departId && departId != 0L)
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE));

        ReturnObject returnObject = grouponService.putGrouponOnShelves(shopId, id);
        if (returnObject.getCode() == ResponseCode.OK) {
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }
    }


    /**
     * 管理员下线spu团购活动
     *
     * @param id
     * @param departId
     * @param shopId
     * @return
     */
    @Audit
    @ResponseBody
    @PutMapping("/shops/{shopId}/groupons/{id}/offshelves")
    public Object putGrouponOffShelves(@PathVariable Long id, @Depart Long departId, @PathVariable Long shopId) {

        if (shopId != departId && departId != 0L)
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE));

        ReturnObject returnObject = grouponService.putGrouponOffShelves(shopId, id);
        if (returnObject.getCode() == ResponseCode.OK) {
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }
    }
}