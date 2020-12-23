package com.ooad.good.service;

import cn.edu.xmu.ooad.util.ReturnObject;
import com.ooad.good.controller.CouponController;
import com.ooad.good.dao.CouponDao;
import com.ooad.good.model.bo.CouponActivity;
import com.ooad.good.model.bo.Presale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CouponService {
    private  static  final Logger logger = LoggerFactory.getLogger(CouponService.class);

    @Autowired
    CouponDao couponDao;

    /**
     * 管理员新建己方优惠活动
     * @param couponActivity
     * @return
     */
    @Transactional
    public ReturnObject insertCouponActivity(CouponActivity couponActivity){
        ReturnObject<CouponActivity>retObj=couponDao.insertCouponActivity(couponActivity);
        return retObj;
    }

    /**
     * 管理员删除己方优惠活动
     * @param id
     * @return
     */
    @Transactional
    public ReturnObject deleteCouponActivity(Long id){

        return couponDao.deleteCouponActivity(id);
    }

    /**
     * 管理员修改己方优惠活动
     * @param couponActivity
     * @return
     */
    @Transactional
    public ReturnObject updateCouponActivity(CouponActivity couponActivity){
        ReturnObject<CouponActivity> retObj=couponDao.updateCouponActivity(couponActivity);
        return retObj;
    }


    /**
     * 上线优惠活动
     * @param id
     * @return
     */
    @Transactional
    public ReturnObject onlineCouponactivity(Long id){
        ReturnObject<CouponActivity>retObj=couponDao.onlineCouponactivity(id);
        return  retObj;
    }

    /**
     * 下线优惠活动
     * @param id
     * @return
     */
    @Transactional
    public ReturnObject offlineCouponactivity(Long id){
        ReturnObject<CouponActivity>retObj=couponDao.offlineCouponactivity(id);
        return  retObj;
    }


}
