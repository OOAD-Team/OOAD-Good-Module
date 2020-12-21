package com.ooad.good.service;

import cn.edu.xmu.ooad.util.ReturnObject;
import com.ooad.good.dao.SkuDao;
import com.ooad.good.model.bo.Groupon;
import com.ooad.good.model.bo.Sku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SkuService {
    private  static  final Logger logger = LoggerFactory.getLogger(SkuService.class);

    @Autowired
    private SkuDao skuDao;

    /**
     * 管理员添加新的sku到spu里
     * @param sku
     * @return
     */
    @Transactional
    public ReturnObject insertSku(Sku sku){
        ReturnObject<Sku> retObj=skuDao.insertSku(sku);
        return retObj;
    }

    /**
     * 店家上架商品
     * @param id
     * @return
     */
    @Transactional
    public ReturnObject onlineSku(Long id){
        ReturnObject<Sku>retObj=skuDao.onlineSku(id);
        return  retObj;
    }

    /**
     * 店家下架商品
     * @param id
     * @return
     */
    @Transactional
    public ReturnObject offlineSku(Long id){
        ReturnObject<Sku>retObj=skuDao.offlineSku(id);
        return  retObj;
    }
}
