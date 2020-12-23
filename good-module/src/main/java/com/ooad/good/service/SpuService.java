package com.ooad.good.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.ooad.good.dao.SpuDao;
import com.ooad.good.model.bo.Spu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpuService {
    private Logger logger = LoggerFactory.getLogger(SpuService.class);
    @Autowired
    private SpuDao spuDao;

    /**
     * 店家新建商品spu
     * @param spu
     * @return
     */
    @Transactional
    public ReturnObject insertSpu(Spu spu) {
        ReturnObject<Spu> retObj = spuDao.insertSpu(spu);
        return retObj;
    }

    /**
     * 店家修改商品spu
     * @param spu
     * @return
     */
    @Transactional
    public ReturnObject updateSpu(Spu spu){
        ReturnObject<Spu>retObj=spuDao.updateSpu(spu);
        return retObj;
    }


    /**
     * 将spu加入品牌
     * @param
     * @return
     */
    @Transactional
    public ReturnObject insertSpuToBrand(Long spuId,Long brandId){
        ReturnObject<Spu>retObj=spuDao.insertSpuToBrand(spuId,brandId);
        return retObj;
    }

    /**
     * 将sku移出品牌
     * @param spuId
     * @param brandId
     * @return
     */
    @Transactional
    public ReturnObject removeSpuFromBrand(Long spuId,Long brandId){
        ReturnObject<Spu>retObj=spuDao.removeSpuFromBrand(spuId,brandId);
        return retObj;
    }

    /**
     * 将spu加入分类
     * @param spuId
     * @param categoryId
     * @return
     */
    @Transactional
    public ReturnObject insertSpuToCategory(Long spuId,Long categoryId){
        ReturnObject<Spu>retObj=spuDao.insertSpuToCategory(spuId,categoryId);
        return retObj;
    }

    /**
     * 将spu移出分类
     * @param spuId
     * @param categoryId
     * @return
     */
    @Transactional
    public ReturnObject removeSpuFromCategory(Long spuId,Long categoryId){
        ReturnObject<Spu>retObj=spuDao.removeSpuFromCategory(spuId,categoryId);
        return retObj;
    }
}
