package com.ooad.good.service;

import cn.edu.xmu.ooad.util.ReturnObject;
import com.ooad.good.controller.PresaleController;
import com.ooad.good.dao.PresaleDao;
import com.ooad.good.model.bo.Presale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PresaleService {
    private  static  final Logger logger = LoggerFactory.getLogger(PresaleService.class);

    @Autowired
    private PresaleDao presaleDao;

    /**
     * 管理员新增sku预售活动
     * @param presale
     * @return
     */
    @Transactional
    public ReturnObject insertPresale(Presale presale){
        ReturnObject<Presale>retObj=presaleDao.insertPresale(presale);
        return  retObj;
    }


    /**
     * 管理员修改sku预售活动
     * @param presale
     * @return
     */
    @Transactional
    public ReturnObject updatePresale(Presale presale){
        ReturnObject<Presale>retObj=presaleDao.updatePresale(presale);
        return retObj;
    }

}
