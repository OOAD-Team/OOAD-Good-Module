package com.ooad.good.dao;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.ooad.good.mapper.GrouponActivityPoMapper;
import com.ooad.good.model.bo.Groupon;
import com.ooad.good.model.bo.Presale;
import com.ooad.good.model.po.GrouponActivityPo;
import com.ooad.good.model.po.GrouponActivityPoExample;
import com.ooad.good.model.po.PresaleActivityPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.swing.*;
import java.util.Objects;

@Repository
public class GrouponDao {
    private static final Logger logger = LoggerFactory.getLogger(GrouponDao.class);

    @Autowired
    private GrouponActivityPoMapper grouponActivityPoMapper;

    /**
     * 管理员对Spu新增团购活动
     * @param groupon
     * @return
     */
    public ReturnObject<Groupon> insertGroupon(Groupon groupon) {
        GrouponActivityPo grouponActivityPo = groupon.gotGrouponPo();
        ReturnObject<Groupon> retObj = null;
        try {
            int ret = grouponActivityPoMapper.insertSelective(grouponActivityPo);
            if (ret == 0) {
                //插入失败
                logger.debug("insertGroupon: insert groupon fail " + grouponActivityPo.toString());
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("新增失败：" + grouponActivityPo.getName()));
            } else {
                //插入成功
                logger.debug("insertGroupon: insert groupon = " + grouponActivityPo.toString());
                groupon.setId(grouponActivityPo.getId());
                retObj = new ReturnObject<>(groupon);
            }
        } catch (DataAccessException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("auth_role.auth_role_name_uindex")) {
                //若有重复的预售活动名则新增失败
                logger.debug("insertGroupon: have same groupon name = " + grouponActivityPo.getName());
                retObj = new ReturnObject<>(ResponseCode.ROLE_REGISTERED, String.format("团购名重复：" + grouponActivityPo.getName()));
            } else {
                // 其他数据库错误
                logger.debug("other sql exception : " + e.getMessage());
                retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
            }
        } catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 管理员修改spu的团购活动
     * @param groupon
     * @return
     */
    public ReturnObject<Groupon>updateGroupon(Groupon groupon) {

        GrouponActivityPo grouponActivityPo = groupon.gotGrouponPo();
        ReturnObject<Groupon> retObj = null;
        GrouponActivityPoExample example = new GrouponActivityPoExample();
        GrouponActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(groupon.getId());

        try {
            int ret =grouponActivityPoMapper.updateByExampleSelective(grouponActivityPo, example);
//
            if (ret == 0) {
                //修改失败
                logger.debug("updateGroupon: update presale fail : " + grouponActivityPo.toString());
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("团购活动id不存在：" + grouponActivityPo.getId()));
            } else {
                //修改成功
                logger.debug("updateGroupon: update groupon = " + grouponActivityPo.toString());
                retObj = new ReturnObject<>();
            }
        } catch (DataAccessException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("auth_role.auth_role_name_uindex")) {
                //若有重复的角色名则修改失败
                logger.debug("updateGroupon: have same groupon name = " + grouponActivityPo.getName());
                retObj = new ReturnObject<>(ResponseCode.ROLE_REGISTERED, String.format("团购活动名重复：" + grouponActivityPo.getName()));
            } else {
                // 其他数据库错误
                logger.debug("other sql exception : " + e.getMessage());
                retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
            }
        } catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }


    /**
     * 管理员上线团购活动
     * @param id
     * @return
     */
    public ReturnObject<Groupon>onlineGroupon(Long id){
        GrouponActivityPo grouponActivityPo=grouponActivityPoMapper.selectByPrimaryKey(id);
        ReturnObject<Groupon>retObj=null;
        if(grouponActivityPo==null||grouponActivityPo.getState()==2){
            //团购活动不存在或已被删除
            logger.info("groupon not exist");
            retObj=new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            return retObj;
        }

        Byte state=1;
        grouponActivityPo.setState(state);
        logger.info("OnlineGroupon: successful: groupon name = " + grouponActivityPo.getName());

        retObj = new ReturnObject<>();
        return retObj;

    }

    /**
     * 管理员下线团购活动
     * @param id
     * @return
     */
    public ReturnObject<Groupon> offlineGroupon(Long id){
        GrouponActivityPo grouponActivityPo=grouponActivityPoMapper.selectByPrimaryKey(id);
        ReturnObject<Groupon>retObj=null;
        if(grouponActivityPo==null||grouponActivityPo.getState()==2){
            //团购活动不存在或已被删除
            logger.info("groupon not exist");
            retObj=new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            return retObj;
        }

        Byte state=0;
        grouponActivityPo.setState(state);
        logger.info("offlineGroupon: successful: groupon name = " + grouponActivityPo.getName());

        retObj = new ReturnObject<>();
        return retObj;

    }
}