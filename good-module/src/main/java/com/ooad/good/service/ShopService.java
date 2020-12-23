package com.ooad.good.service;

import cn.edu.xmu.ooad.util.ReturnObject;
import com.ooad.good.dao.ShopDao;
import com.ooad.good.model.bo.Shop;
import com.ooad.good.model.vo.shop.ShopVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: Chaoyang Deng
 * @Date: 2020/12/9 上午8:46
 */

@Service
public class ShopService {
    private Logger logger = LoggerFactory.getLogger(ShopService.class);

    @Autowired
    ShopDao shopDao;
    /**
     * 增加店铺
     * @param shop
     * @return
     */
    @Transactional
    public ReturnObject insertShop(Shop shop) {
        ReturnObject<Shop> retObj = shopDao.insertShop(shop);
        return retObj;
    }
    /**
     * 查询
     * @return
     */
    public ReturnObject<List> getAllShopStates() {
        ReturnObject<List> retObj = shopDao.getAllShopStates();

        return retObj;
    }
    /**
     * 修改任意店铺信息
     * @param id 店铺 id
     * @param vo ShopEditVo 对象
     * @return 返回对象 ReturnObject
     */
    @Transactional
    public ReturnObject<Object> modifyShopInfo(Long id, ShopVo vo) {
        return shopDao.modifyShopByVo(id, vo);
    }
    /**
     * 删除店铺
     * @return
     */
    @Transactional
    public ReturnObject deleteShop(Long id) {
        return shopDao.deleteShop(id);
    }
    /**
     * 审核店铺信息
     * @param id
     * @return
     */
    @Transactional
    public ReturnObject auditShopInfo(Long id,boolean conJudge)
    {
        return shopDao.auditShopInfo(id,conJudge);
    }
    /**
     * 上线店铺
     * @return
     */
    @Transactional
    public ReturnObject onshelvesShop(Long id) {
        return shopDao.onshelvesShop(id);
    }
    /**
     * 下线店铺
     * @return
     */
    @Transactional
    public ReturnObject offshelvesShop(Long id) {
        return shopDao.offshelvesShop(id);
    }
}
