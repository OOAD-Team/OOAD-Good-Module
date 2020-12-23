package com.ooad.good.dao;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.ooad.good.mapper.FlashSaleItemPoMapper;
import com.ooad.good.mapper.FlashSalePoMapper;
import com.ooad.good.mapper.SkuPoMapper;
import com.ooad.good.model.bo.FlashSale;
import com.ooad.good.model.bo.FlashSaleItem;
import com.ooad.good.model.bo.Sku;
import com.ooad.good.model.po.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @AuthorId: 24320182203185
 * @Author: Chaoyang Deng
 * @Date: 2020/12/15 下午9:48
 */
@Repository
public class FlashSaleDao {
    private static final Logger logger = LoggerFactory.getLogger(FlashSaleDao.class);

    @Autowired
    private FlashSaleItemPoMapper flashSaleitemPoMapper;
    @Autowired
    private FlashSalePoMapper flashSalePoMapper;
    @Autowired
    private SkuPoMapper skuPoMapper;

    /**
     * 通过时间段ID获取秒杀活动
     *
     * @param timeSeqId
     * @return
     */
    public ReturnObject<List> getflashSaleId(Long timeSeqId) {
        List<FlashSalePo> flashPos = null;
        FlashSalePoExample example = new FlashSalePoExample();
        FlashSalePoExample.Criteria criteria = example.createCriteria();
        criteria.andTimeSegIdEqualTo(timeSeqId);
        try {
            flashPos = flashSalePoMapper.selectByExample(example);
        } catch (DataAccessException e) {
            logger.error("getAllFlashSales: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        List<Long> IdList = new ArrayList<>(flashPos.size());
        //System.out.println(ShopPos.size());
        if (flashPos.size() == 0) {
            logger.error("getShopStates: 数据库不存在当前时间段秒杀活动 ");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        for (FlashSalePo po : flashPos) {
            if (po.getState() != 2) {
                Long nowId;
                logger.info("getAllFlashSales: " + po);
                nowId = po.getId();
                IdList.add(nowId);
            }
        }
        return new ReturnObject<>(IdList);

    }

    /**
     * 通过秒杀活动Id获取秒杀活动
     *
     * @param flashId
     * @return
     */
    public ReturnObject<List> getflashSaleById(List<Long> flashId) {
        FlashSalePo flashPo = null;
        FlashSaleItemPoExample example = new FlashSaleItemPoExample();
        FlashSaleItemPoExample.Criteria criteria = example.createCriteria();
        if (flashId.size() == 0) return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        List<FlashSaleItem> flashSaleSimpleList = new ArrayList<>(flashId.size());

        for (Long id : flashId) {
            List<FlashSaleItemPo> pos = null;
            criteria.andSaleIdEqualTo(id);
            // FlashSaleItemPo po;
            try {
                flashPo = flashSalePoMapper.selectByPrimaryKey(id);
                pos = flashSaleitemPoMapper.selectByExample(example);
            } catch (DataAccessException e) {
                logger.error("getAllFlashSales: DataAccessException:" + e.getMessage());
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
            }
            if (flashPo.getState() != 2) {  //未被删除
                for (FlashSaleItemPo item : pos) {
                    if (item != null) {
                        logger.info("getAllFlashSales: " + item);
                        FlashSaleItem nowFlash = new FlashSaleItem(item);
                        Sku nowSku = getSkuById(item.getGoodsSkuId()).getData();
                        nowFlash.setGoodsSku(nowSku);
                        flashSaleSimpleList.add(nowFlash);
                    }
                }
            }
        }
        return new ReturnObject<>(flashSaleSimpleList);
    }

    /**
     * 通过skuId查询sku对象
     *
     * @param skuId
     * @return
     */
    public ReturnObject<Sku> getSkuById(Long skuId) {
        SkuPo po = null;
        try {
            po = skuPoMapper.selectByPrimaryKey(skuId);
        } catch (DataAccessException e) {
            logger.error("getSkuById: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if (po != null) {
            logger.info("getSkuById: " + po);
            Sku nowSku = new Sku(po);
            return new ReturnObject<>(nowSku);
        } else {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }

    /**
     * 通过flashSaleId查询flashSale对象
     *
     * @param flashId
     * @return
     */
    public ReturnObject<FlashSale> getFlashSaleById(Long flashId) {
        FlashSalePo po = null;
        try {
            po = flashSalePoMapper.selectByPrimaryKey(flashId);
        } catch (DataAccessException e) {
            logger.error("getFlashSaleById: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if (po != null && po.getState() != 2) {
            logger.info("getFlashSaleById: " + po);
            FlashSale nowFlashSale = new FlashSale(po);
            return new ReturnObject<>(nowFlashSale);
        } else {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }

    /**
     * 新建秒杀活动
     *
     * @param flashSale
     * @return
     */
    public ReturnObject<FlashSale> insertFlashsale(FlashSale flashSale) {
        FlashSalePo flashSalePo = flashSale.gotFlashSalePo();
        ReturnObject<FlashSale> retObj = null;
        try {
            int ret = flashSalePoMapper.insertSelective(flashSalePo);
            if (ret == 0) {
                //插入失败
                logger.debug("insertFlashsale: insert flashsale fail" + flashSalePo.toString());
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("新增失败：" + flashSalePo));
            } else {
                //插入成功
                logger.debug("insertFlashsale: insert flashsale = " + flashSalePo.toString());
                flashSale.setId(flashSalePo.getId());
                retObj = new ReturnObject<>(flashSale);
            }
        } catch (DataAccessException e) {
            // 数据库错误
            logger.debug("other sql exception : " + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 向秒杀活动添加商品
     *
     * @param flashSaleItem
     * @return
     */
    public ReturnObject<FlashSaleItem> insertSku(FlashSaleItem flashSaleItem) {
        FlashSaleItemPo flashPo = flashSaleItem.gotFlashSaleItemPo();
        ReturnObject<FlashSaleItem> retObj = null;
        try {
            int ret = flashSaleitemPoMapper.insertSelective(flashPo);
            if (ret == 0) {
                //插入失败
                logger.debug("insertFlashSaleItem: insert flash sale item fail" + flashPo.toString());
                retObj = new ReturnObject<>(ResponseCode.FIELD_NOTVALID, String.format("新增失败：" + flashPo.getId()));
            } else {
                //插入成功
                logger.debug("insertFlashSaleItem: insert flash sale item = " + flashPo.toString());
                flashSaleItem.setId(flashPo.getId());
                retObj = new ReturnObject<>(flashSaleItem);
            }
        } catch (DataAccessException e) {
            // 其他数据库错误
            logger.debug("other sql exception : " + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 根据id查询优惠活动
     * @param id
     * @return
     */
    public ReturnObject<FlashSalePo> selectByFlashsaleId(Long id) {
        FlashSalePo po = flashSalePoMapper.selectByPrimaryKey(id);
        if(po!=null) {
            return new ReturnObject<>(po);
        }
        return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
    }

    /**
     * 删除秒杀商品
     *
     * @param fid
     * @param id
     * @return
     */
    public ReturnObject deleteFlashsaleItem(Long fid, Long id) {

        FlashSaleItemPoExample example = new FlashSaleItemPoExample();
        FlashSaleItemPoExample.Criteria criteria = example.createCriteria();
        logger.info("delete flash sale item: id= " + id);
        criteria.andSaleIdEqualTo(fid);
        criteria.andIdEqualTo(id);
        int ret;
        ReturnObject retObj = null;
        try {
            logger.info("秒杀活动商品 id = " + id + "进行删除");
            ret = flashSaleitemPoMapper.deleteByExample(example);
            if (ret != 0) {
                logger.info("秒杀活动商品 id = " + id + "已被删除");
                retObj = new ReturnObject<>(ret);
            } else {
                logger.info("秒杀活动商品 id = " + id + "未被删除");
                retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
            }

        } catch (DataAccessException e) {
            // 数据库错误
            logger.error("数据库错误：" + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 属未知错误
            logger.error("严重错误：" + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的未知错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 上架秒杀活动
     *
     * @param id
     * @return
     */
    public ReturnObject onshelvesflashSale(Long id) {
        FlashSalePo po = new FlashSalePo();
        ReturnObject<Object> retObj = null;
        FlashSalePoExample flashPo = new FlashSalePoExample();
        FlashSalePoExample.Criteria criteria = flashPo.createCriteria();
        criteria.andIdEqualTo(id);
        try {
            po = flashSalePoMapper.selectByPrimaryKey(id);
        } catch (DataAccessException e) {
            logger.error("get flash sale id= ", id, " : DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if (po == null || po.getState() == 2) {
            logger.info("秒杀活动不存在或已被删除：id = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else if (po.getState() != 0) {
            logger.info("秒杀活动不能被上架（未处于下架状态中）：id = " + id);
            return new ReturnObject<>(ResponseCode.USER_HASSHOP);
        }

        int ret;
        Byte state = 1;
        FlashSalePo newPo = new FlashSalePo();
        newPo.setState(state);
        newPo.setId(id);
        newPo.setGmtCreate(po.getGmtCreate());
        newPo.setGmtModified(LocalDateTime.now());
        logger.info("onshelves flash sale: id= " + id);
        try {
            ret = flashSalePoMapper.updateByPrimaryKeySelective(newPo);
            logger.info("秒杀活动 id = " + id + " 的状态修改为 " + newPo.getState());
            retObj = new ReturnObject<>();
        } catch (DataAccessException e) {
            // 数据库错误
            logger.error("数据库错误：" + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 属未知错误
            logger.error("严重错误：" + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的未知错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 下架秒杀活动
     *
     * @param id
     * @return
     */
    public ReturnObject offshelvesflashSale(Long id) {
        FlashSalePo po = new FlashSalePo();
        ReturnObject<Object> retObj = null;
        FlashSalePoExample flashPo = new FlashSalePoExample();
        FlashSalePoExample.Criteria criteria = flashPo.createCriteria();
        criteria.andIdEqualTo(id);
        try {
            po = flashSalePoMapper.selectByPrimaryKey(id);
        } catch (DataAccessException e) {
            logger.error("get flash sale id= ", id, " : DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if (po == null || po.getState() == 2) {
            logger.info("秒杀活动不存在或已被删除：id = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else if (po.getState() != 1) {
            logger.info("秒杀活动不能被下架（未处于上架状态中）：id = " + id);
            return new ReturnObject<>(ResponseCode.USER_HASSHOP);
        }

        int ret;
        Byte state = 0;
        FlashSalePo newPo = new FlashSalePo();
        newPo.setState(state);
        newPo.setId(id);
        newPo.setGmtCreate(po.getGmtCreate());
        newPo.setGmtModified(LocalDateTime.now());
        logger.info("offshelves flash sale: id= " + id);
        try {
            ret = flashSalePoMapper.updateByPrimaryKeySelective(newPo);
            logger.info("秒杀活动 id = " + id + " 的状态修改为 " + newPo.getState());
            retObj = new ReturnObject<>();
        } catch (DataAccessException e) {
            // 数据库错误
            logger.error("数据库错误：" + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 属未知错误
            logger.error("严重错误：" + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的未知错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 删除秒杀活动
     *
     * @param id
     * @return
     */

    public ReturnObject deleteflashSale(Long id) {
        FlashSalePo po = new FlashSalePo();
        ReturnObject<Object> retObj = null;
        FlashSalePoExample flashPo = new FlashSalePoExample();
        FlashSalePoExample.Criteria criteria = flashPo.createCriteria();
        criteria.andIdEqualTo(id);
        try {
            po = flashSalePoMapper.selectByPrimaryKey(id);
        } catch (DataAccessException e) {
            logger.error("get flash sale id= ", id, " : DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if (po == null || po.getState() == 2) {
            logger.info("秒杀活动不存在或已被删除：id = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        int ret;
        Byte state = 2;
        FlashSalePo newPo = new FlashSalePo();
        newPo.setState(state);
        newPo.setId(id);
        newPo.setGmtCreate(po.getGmtCreate());
        newPo.setGmtModified(LocalDateTime.now());
        logger.info("delete flash sale: id= " + id);
        try {
            ret = flashSalePoMapper.updateByPrimaryKeySelective(newPo);
            logger.info("秒杀活动 id = " + id + " 已被删除");
            retObj = new ReturnObject<>();
        } catch (DataAccessException e) {
            // 数据库错误
            logger.error("数据库错误：" + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 属未知错误
            logger.error("严重错误：" + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的未知错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 管理员修改秒杀活动
     *
     * @param flashSale
     * @return
     */
    public ReturnObject modifyFlashSale(FlashSale flashSale) {
        FlashSalePo flashSalePo = flashSale.gotFlashSalePo();
        ReturnObject<FlashSale> retObj = null;
        try {
            int ret = flashSalePoMapper.updateByPrimaryKeySelective(flashSalePo);
            if (ret == 0) {
                //插入失败
                logger.debug("updateFlashsale: update flashsale fail" + flashSalePo.toString());
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("修改失败：" + flashSalePo));
            } else {
                //插入成功
                logger.debug("updateFlashsale: update flashsale = " + flashSalePo.toString());
                flashSale.setId(flashSalePo.getId());
                retObj = new ReturnObject<>(flashSale);
            }
        } catch (DataAccessException e) {
            // 数据库错误
            logger.debug("other sql exception : " + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 删除某一优惠活动包含的商品
     * @param id
     * @return
     */
    public ReturnObject deleteBySaleId(Long id) {
        try {
            FlashSaleItemPoExample example = new FlashSaleItemPoExample();
            FlashSaleItemPoExample.Criteria criteria = example.createCriteria();
            criteria.andSaleIdEqualTo(id);
            flashSaleitemPoMapper.deleteByExample(example);
            return new ReturnObject(ResponseCode.OK);//因为是级联删除，所以id不一定会有对应的item，即使删除0行也正确
        } catch (DataAccessException e) {
            // 其他数据库错误
            logger.debug("other sql exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    /**
     * 删除某一时间段中的秒杀活动
     *
     * @param id
     * @return
     */
    public ReturnObject deleteSegmentFlashsale(Long id) {
        try {
            FlashSalePoExample example = new FlashSalePoExample();
            FlashSalePoExample.Criteria criteria = example.createCriteria();
            criteria.andTimeSegIdEqualTo(id);
            List<FlashSalePo> list = flashSalePoMapper.selectByExample(example);
            for (FlashSalePo po : list) {
                deleteBySaleId(po.getId());
            }
            int ret = flashSalePoMapper.deleteByExample(example);
            return new ReturnObject(ResponseCode.OK);
        } catch (DataAccessException e) {
            // 数据库错误
            logger.debug("other sql exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 其他错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    /**
     * 查询秒杀时间段是否存在
     * @param time
     * @param id
     * @return
     */
    public ReturnObject<FlashSalePo> selectByFlashDateAndSegId(LocalDateTime time, Long id) {
        FlashSalePoExample example = new FlashSalePoExample();
        FlashSalePoExample.Criteria criteria = example.createCriteria();
        criteria.andFlashDateEqualTo(time);
        criteria.andTimeSegIdEqualTo(id);
        Byte type = 2;
        criteria.andStateNotEqualTo(type);
        List<FlashSalePo> pos = flashSalePoMapper.selectByExample(example);
        if(pos.size()==0) {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        return new ReturnObject<>(pos.get(0));
    }
}