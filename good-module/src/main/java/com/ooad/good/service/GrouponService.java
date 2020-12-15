package com.ooad.good.service;

import cn.edu.xmu.ooad.util.ReturnObject;
import com.ooad.good.controller.PresaleController;
import com.ooad.good.dao.GrouponDao;
import com.ooad.good.model.bo.CouponActivity;
import com.ooad.good.model.bo.Groupon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GrouponService {
    private  static  final Logger logger = LoggerFactory.getLogger(GrouponService.class);

    @Autowired
    private GrouponDao grouponDao;

    /**
     * 管理员对Spu新增团购活动
     * @param groupon
     * @return
     */
    @Transactional
    public ReturnObject insertGroupon(Groupon groupon){
        ReturnObject<Groupon>retObj=grouponDao.insertGroupon(groupon);
        return retObj;
    }

    /**
     * 管理员修改spu的团购活动
     * @param groupon
     * @return
     */
    @Transactional
    public ReturnObject updateGroupon(Groupon groupon){
        ReturnObject<Groupon>retObj=grouponDao.updateGroupon(groupon);
        return retObj;
    }
}
