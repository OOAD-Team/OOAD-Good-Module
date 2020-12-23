package com.ooad.good.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.ooad.good.mapper.BrandPoMapper;
import com.ooad.good.mapper.GoodsCategoryPoMapper;
import com.ooad.good.mapper.SkuPoMapper;
import com.ooad.good.mapper.SpuPoMapper;
import com.ooad.good.model.bo.Brand;
import com.ooad.good.model.bo.Spu;
import com.ooad.good.model.po.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
public class SpuDao {
    private Logger logger = LoggerFactory.getLogger(SpuDao.class);
    @Autowired
    private SpuPoMapper spuPoMapper;

    @Autowired
    private SkuPoMapper skuPoMapper;

    @Autowired
    private BrandPoMapper brandPoMapper;

    @Autowired
    private GoodsCategoryPoMapper categoryPoMapper;

    /**
     * 店家新建商品spu
     *
     * @param spu
     * @return
     */
    public ReturnObject<Spu> insertSpu(Spu spu) {
        SpuPo spuPo = spu.gotSpuPo();
        ReturnObject<Spu> retObj = null;
        try {
            int ret = spuPoMapper.insertSelective(spuPo);
            if (ret == 0) {
                //插入失败
                logger.debug("insertRole: insert spu fail " + spuPo.toString());
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("新增失败：" + spuPo.getName()));
            } else {
                //插入成功
                logger.debug("insertRole: insert spu = " + spuPo.toString());
                spu.setId(spuPo.getId());
                retObj = new ReturnObject<>(spu);
            }
        } catch (DataAccessException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("auth_role.auth_role_name_uindex")) {
                //若有重复的角色名则新增失败
                logger.debug("updateRole: have same spu name = " + spuPo.getName());
                retObj = new ReturnObject<>(ResponseCode.ROLE_REGISTERED, String.format("spu名重复：" + spuPo.getName()));
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
     * 店家修改spu
     *
     * @param spu
     * @return
     */
    public ReturnObject<Spu> updateSpu(Spu spu) {
        SpuPo spuPo = spu.gotSpuPo();
        ReturnObject<Spu> retObj = null;
        SpuPoExample example = new SpuPoExample();
        SpuPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(spu.getId());

        try {
            int ret = spuPoMapper.updateByExampleSelective(spuPo, example);
//            int ret = roleMapper.updateByPrimaryKeySelective(rolePo);
            if (ret == 0) {
                //修改失败
                logger.debug("updateRole: update spu fail : " + spuPo.toString());
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("spu id不存在：" + spuPo.getId()));
            } else {
                //修改成功
                logger.debug("updateRole: update spu = " + spuPo.toString());
                retObj = new ReturnObject<>();
            }
        } catch (DataAccessException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("auth_role.auth_role_name_uindex")) {
                //若有重复的角色名则修改失败
                logger.debug("updateRole: have same spu name = " + spuPo.getName());
                retObj = new ReturnObject<>(ResponseCode.ROLE_REGISTERED, String.format("spu名重复：" + spuPo.getName()));
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
     * 将spu加入品牌
     * @param
     * @return
     */
    public ReturnObject<Spu> insertSpuToBrand(Long spuId,Long brandId) {

        SpuPo spuPo = spuPoMapper.selectByPrimaryKey(spuId);
        BrandPo brandPo = brandPoMapper.selectByPrimaryKey(brandId);
        ReturnObject<Spu> retObj = null;

        if (brandPo == null || spuPo == null) {
            //品牌or spu不存在
            logger.info(" brand or spu not exist");
           retObj= new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
           return retObj;
        }

        spuPo.setBrandId(brandId);
        logger.info("insertSpuToBrand: successful: " + spuPo.getName() + " insert to " + brandPo.getName());

        retObj = new ReturnObject<>();
        return retObj;
    }

    /**
     * 将sku移出品牌
     * @param spuId
     * @param brandId
     * @return
     */
    public ReturnObject<Spu> removeSpuFromBrand(Long spuId,Long brandId) {

        SpuPo spuPo = spuPoMapper.selectByPrimaryKey(spuId);
        BrandPo brandPo = brandPoMapper.selectByPrimaryKey(brandId);
        ReturnObject<Spu> retObj = null;

        if (brandPo == null || spuPo == null) {
            //品牌or spu不存在
            logger.info(" brand or spu not exist");
            retObj= new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            return retObj;
        }

        spuPo.setBrandId(null);
        logger.info("removeSpuFromBrand: successful: " + spuPo.getName() + " remove from " + brandPo.getName());

        retObj = new ReturnObject<>();
        return retObj;
    }

    /**
     * 将spu加入分类
     * @param spuId
     * @param categoryId
     * @return
     */
    public ReturnObject<Spu> insertSpuToCategory(Long spuId,Long categoryId) {

        SpuPo spuPo = spuPoMapper.selectByPrimaryKey(spuId);
        GoodsCategoryPo categoryPo=categoryPoMapper.selectByPrimaryKey(categoryId);
        ReturnObject<Spu> retObj = null;

        if (categoryPo == null || spuPo == null) {
            //商品类目or spu不存在
            logger.info("category or spu not exist");
            retObj= new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            return retObj;
        }

        spuPo.setBrandId(categoryId);
        logger.info("insertSpuToBrand: successful: " + spuPo.getName() + " insert to " + categoryPo.getName());

        retObj = new ReturnObject<>();
        return retObj;
    }


    /**
     * 将spu移出分类
     * @param spuId
     * @param categoryId
     * @return
     */
    public ReturnObject<Spu> removeSpuFromCategory(Long spuId,Long categoryId) {

        SpuPo spuPo = spuPoMapper.selectByPrimaryKey(spuId);
        GoodsCategoryPo categoryPo=categoryPoMapper.selectByPrimaryKey(categoryId);
        ReturnObject<Spu> retObj = null;

        if (categoryPo == null || spuPo == null) {
            //商品类目or spu不存在
            logger.info(" category or spu not exist");
            retObj= new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            return retObj;
        }

        spuPo.setCategoryId(null);
        logger.info("removeSpuFromCategory: successful: " + spuPo.getName() + " remove from " + categoryPo.getName());

        retObj = new ReturnObject<>();
        return retObj;
    }
    /**
     * 获取店铺id下的spuid
     * @param id
     * @return
     */
    public ReturnObject<List> getAllSpuIdByShopId(Long id) {
        SpuPoExample example = new SpuPoExample();
        SpuPoExample.Criteria criteria = example.createCriteria();
        criteria.andShopIdEqualTo(id);
        List<SpuPo> pos = null;
        try {
            pos = spuPoMapper.selectByExample(example);
        } catch (Exception e) {
            logger.debug("other sql exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        List<Long> ids = null;
        if(pos!=null) {
            for(SpuPo p : pos){
                ids.add(p.getId());
            }
        }
        return new ReturnObject<>(ids);
    }

    /**
     * 管理员逻辑删除对应店铺shopid的相应商品id的商品
     * @param shopId
     * @param id
     */
    public ReturnObject<Object> deleteGoodsSpu(Long shopId, Long id) {
        ReturnObject<Object> returnObject=null;
        try{
            SpuPo spuPo=spuPoMapper.selectByPrimaryKey(id);
            if(spuPo==null||!spuPo.getShopId().equals(shopId)||spuPo.getDisabled().equals((byte)1))
                return returnObject=new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            SkuPoExample skuPoExample=new SkuPoExample();
            SkuPoExample.Criteria criteria=skuPoExample.createCriteria();
            criteria.andDisabledEqualTo((byte)0);
            List<SkuPo>skuPos=skuPoMapper.selectByExample(skuPoExample);

            if(skuPos==null)
            {
                skuPoMapper.deleteByPrimaryKey(id);
                return new ReturnObject<>(ResponseCode.OK);
            }

            for(SkuPo skuPo:skuPos)
            {
                SkuPoExample skuExample=new SkuPoExample();
                SkuPoExample.Criteria skucriteria=skuExample.createCriteria();
                criteria.andIdEqualTo(skuPo.getId());
                criteria.andGoodsSpuIdEqualTo(id);
                skuPo.setDisabled((byte)6);
                int ret =skuPoMapper.updateByExample(skuPo,skuExample);
                if(ret==0)
                {
                    logger.debug("delete sku id:"+skuPo.getId()+"fail");
                    returnObject=new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
                }
            }
            spuPoMapper.deleteByPrimaryKey(id);
            return new ReturnObject<>(ResponseCode.OK);
        }
        catch (DataAccessException e) {
            logger.error("数据库错误：" + e.getMessage());
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 其他 Exception 即属未知错误
            logger.error("严重错误：" + e.getMessage());
            returnObject= new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的未知错误：%s", e.getMessage()));
        }
        return returnObject;
    }

}
