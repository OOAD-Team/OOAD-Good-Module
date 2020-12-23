package com.ooad.good.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.ooad.good.model.bo.CouponActivity;
import com.ooad.good.model.vo.couponActivity.CouponActivityVo;
import com.ooad.good.model.vo.couponActivity.UpdateCouponActivityVo;
import com.ooad.good.service.CouponService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Api(value="优惠服务",tags="coupon")
@RestController
@RequestMapping(value = "/goods", produces = "application/json;charset=UTF-8")
public class CouponController {

    private  static  final Logger logger = LoggerFactory.getLogger(CouponController.class);

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private CouponService couponService;

    /**
     * 管理员新建己方优惠活动
     * @param vo
     * @param bindingResult
     * @return
     */
    @Audit
    @PostMapping("/couponactivities")
    public Object insertCouponActivity(@Validated @RequestBody CouponActivityVo vo, BindingResult bindingResult){
        logger.debug("insert couponactivity :");
        //校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            logger.debug("validate fail");
            return returnObject;
        }

        CouponActivity couponActivity = vo.createCouponActivity();
        couponActivity.setGmtCreate(LocalDateTime.now());
        ReturnObject retObject = couponService.insertCouponActivity(couponActivity);
        if (retObject.getData() != null) {
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.getRetObject(retObject);
        } else {
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }
    }

    /**
     * 管理员删除己方优惠活动
     * @param id
     * @return
     */
    @Audit
    @DeleteMapping("/couponactivities/{id}")
    public Object deleteCouponActivity(@PathVariable("id") Long id){
        logger.debug("delete couponActivity: id= "+id);

            ReturnObject returnObject = couponService.deleteCouponActivity(id);
            return Common.decorateReturnObject(returnObject);

    }

    /**
     * 管理员修改己方优惠活动
     * @param id
     * @param vo
     * @param bindingResult
     * @return
     */
    @Audit
    @PutMapping("/couponactivities/{id}")
    public Object updateCouponActivity(@PathVariable("id") Long id, @Validated @RequestBody UpdateCouponActivityVo vo,BindingResult bindingResult){
        logger.debug("update couponActivity: id= "+id);
        //校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            return returnObject;
        }
        CouponActivity couponActivity=vo.createCouponActivity();
        couponActivity.setId(id);
        couponActivity.setGmtModified(LocalDateTime.now());
        ReturnObject retObject=couponService.updateCouponActivity(couponActivity);
        if (retObject.getData() != null) {
            return Common.getRetObject(retObject);
        } else {
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }
    }


    /**
     * 上线优惠活动
     * @param id
     * @return
     */
    @Audit
    @PutMapping("couponactivities/{id}/onshelves")
    public Object onlinePresale(@PathVariable("id")Long id){

        logger.debug("onlineCouponactivities: id ="+id);

        ReturnObject retObject=couponService.onlineCouponactivity(id);
        return Common.getRetObject(retObject);

    }

    /**
     * 下线优惠活动
     * @param id
     * @return
     */
    @Audit
    @PutMapping("couponactivities/{id}/offshelves")
    public Object offlinePresale(@PathVariable("id")Long id){

        logger.debug("offlineCouponactivities: id ="+id);

        ReturnObject retObject=couponService.offlineCouponactivity(id);
        return Common.getRetObject(retObject);

    }

}
