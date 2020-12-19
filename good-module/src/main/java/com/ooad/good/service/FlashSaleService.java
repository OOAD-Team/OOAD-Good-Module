package com.ooad.good.service;

import cn.edu.xmu.ooad.util.ReturnObject;
import com.ooad.good.dao.FlashSaleDao;
import com.ooad.good.dao.SkuDao;
import com.ooad.good.model.bo.FlashSale;
import com.ooad.good.model.bo.FlashSaleItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @AuthorId: 24320182203185
 * @Author: Chaoyang Deng
 * @Date: 2020/12/15 下午9:46
 */
@Service
public class FlashSaleService {

    @Autowired
    FlashSaleDao flashsaleDao;
    @Autowired
    SkuDao skuDao;
    /**
     * 向秒杀活动添加商品
     * @param flashSaleItem
     * @return
     */
    @Transactional
    public ReturnObject insertSkuToFlash(FlashSaleItem flashSaleItem) {
        ReturnObject<FlashSaleItem> retObj = flashsaleDao.insertSku(flashSaleItem);
        return retObj;
    }

    /**
     * 查询秒杀活动
     * @param id
     * @return
     */
    @Transactional
    public ReturnObject<List> getAllFlashsale(Long id) {
        ReturnObject<List> returnId=flashsaleDao.getflashSaleId(id);

        if(returnId.getCode().equals("RESOURCE_ID_NOTEXIST"))return returnId;
        List<Long> flashId=returnId.getData();
        ReturnObject<List> returnObject=flashsaleDao.getflashSaleById(flashId);
        return returnObject;
    }
    /**
     * 新建秒杀活动
     * @param flashSale
     * @return
     */
    @Transactional
    public ReturnObject insertFlashsale(FlashSale flashSale) {
        ReturnObject<FlashSale> retObj = flashsaleDao.insertFlashsale(flashSale);
        return retObj;
    }
    /**
     * 删除秒杀活动中商品
     * @param id
     * @return
     */
    @Transactional
    public ReturnObject deleteFlashsaleItem(Long fid, Long id) {
        ReturnObject returnFid=flashsaleDao.getFlashSaleById(fid);
        if(returnFid.getCode().equals("RESOURCE_ID_NOTEXIST"))return returnFid;
        return flashsaleDao.deleteFlashsaleItem(fid,id);
    }
    /**
     * 上架秒杀活动
     * @param id
     * @return
     */
    @Transactional
    public ReturnObject onshelvesflashSale(Long id) {
        ReturnObject retObj=flashsaleDao.onshelvesflashSale(id);
        return retObj;
    }
    /**
     * 下架秒杀活动
     * @param id
     * @return
     */
    @Transactional
    public ReturnObject offshelvesflashSale(Long id) {
        ReturnObject retObj=flashsaleDao.offshelvesflashSale(id);
        return retObj;
    }
    /**
     * 删除秒杀活动
     * @param id
     * @return
     */
    @Transactional
    public ReturnObject deleteflashSale(Long id) {
        ReturnObject retObj=flashsaleDao.deleteflashSale(id);
        return retObj;
    }
}
