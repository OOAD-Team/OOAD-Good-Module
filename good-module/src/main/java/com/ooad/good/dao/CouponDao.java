package com.ooad.good.dao;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.ooad.good.controller.CouponController;
import com.ooad.good.mapper.CouponActivityPoMapper;
import com.ooad.good.model.bo.CouponActivity;
import com.ooad.good.model.po.CouponActivityPo;
import com.ooad.good.model.po.CouponActivityPoExample;
import com.ooad.good.model.po.GoodsCategoryPoExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class CouponDao {
    private  static  final Logger logger = LoggerFactory.getLogger(CouponDao.class);

    @Autowired
    private CouponActivityPoMapper couponActivityPoMapper;

    /**
     * 管理员新建己方优惠活动
     * @param couponActivity
     * @return
     */
    public ReturnObject<CouponActivity>insertCouponActivity(CouponActivity couponActivity){

        CouponActivityPo couponActivityPo=couponActivity.gotCouponActivityPo();
        ReturnObject<CouponActivity>retObj=null;
        try{
            int ret = couponActivityPoMapper.insertSelective(couponActivityPo);
            if (ret == 0) {
                //插入失败
                logger.debug("insertCouponActivity: insert couponActivity fail " + couponActivityPo.toString());
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("新增失败：" + couponActivityPo.getName()));
            } else {
                //插入成功
                logger.debug("insertRole: insert couponActivity = " + couponActivityPo.toString());
                couponActivity.setId(couponActivityPo.getId());
                retObj = new ReturnObject<>(couponActivity);
            }
        }
        catch (DataAccessException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("auth_role.auth_role_name_uindex")) {
                //若有重复的couponActivity名则新增失败
                logger.debug("insertCouponActivity: have same role name = " + couponActivityPo.getName());
                retObj = new ReturnObject<>(ResponseCode.ROLE_REGISTERED, String.format("couponActivity名重复：" + couponActivityPo.getName()));
            } else {
                // 其他数据库错误
                logger.debug("other sql exception : " + e.getMessage());
                retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
            }
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }


    /**
     * 管理员删除己方优惠活动
     * @param id
     * @return
     */
    public ReturnObject<Object>deleteCouponActivity(Long id){
        ReturnObject<Object> retObj = null;
        CouponActivityPoExample example = new CouponActivityPoExample();
        CouponActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        try {
            int ret = couponActivityPoMapper.deleteByExample(example);
            if (ret == 0) {

                logger.info("deleteCouponActivity: id not exist = " + id);
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("优惠活动id不存在：" + id));
            } else {
                logger.info("deleteCouponActivity: delete CouponActivity id = " + id);
            }

            retObj = new ReturnObject<>();
            return retObj;
        } catch (DataAccessException e) {
            logger.error("selectAllCouponActivity: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }


    public ReturnObject<CouponActivity>updateCouponActivity(CouponActivity couponActivity){

        CouponActivityPo couponActivityPo=couponActivity.gotCouponActivityPo();
        ReturnObject<CouponActivity>retObj=null;
        CouponActivityPoExample example=new CouponActivityPoExample();
        CouponActivityPoExample.Criteria criteria=example.createCriteria();
        criteria.andIdEqualTo(couponActivity.getId());

        try{
            int ret = couponActivityPoMapper.updateByExampleSelective(couponActivityPo, example);
//            int ret = roleMapper.updateByPrimaryKeySelective(rolePo);
            if (ret == 0) {
                //修改失败
                logger.debug("updateCouponActivity: update CouponActivity fail : " + couponActivityPo.toString());
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("优惠活动id不存在：" + couponActivityPo.getId()));
            } else {
                //修改成功
                logger.debug("updateCouponActivity: update CouponActivity = " + couponActivityPo.toString());
                retObj = new ReturnObject<>();
            }
        }
        catch (DataAccessException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("auth_role.auth_role_name_uindex")) {
                //若有重复的角色名则修改失败
                logger.debug("updateCouponActivity: have same CouponActivity name = " + couponActivityPo.getName());
                retObj = new ReturnObject<>(ResponseCode.ROLE_REGISTERED, String.format("优惠活动名重复：" + couponActivityPo.getName()));
            } else {
                // 其他数据库错误
                logger.debug("other sql exception : " + e.getMessage());
                retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
            }
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }

}
