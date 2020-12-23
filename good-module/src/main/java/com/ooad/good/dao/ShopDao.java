package com.ooad.good.dao;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.ooad.good.mapper.ShopPoMapper;
import com.ooad.good.model.bo.Shop;
import com.ooad.good.model.po.ShopPo;
import com.ooad.good.model.po.ShopPoExample;
import com.ooad.good.model.vo.shop.ShopVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: Chaoyang Deng
 * @Date: 2020/12/9 上午8:48
 */
@Repository
public class ShopDao {
    private static final Logger logger = LoggerFactory.getLogger(ShopDao.class);

    @Autowired
    private ShopPoMapper shopMapper;
    @Autowired
    private SpuDao spuDao;

    /**
     * 查询店铺状态
     * @return
     */
    /*
    public ReturnObject<List> getAllShopStates() {
        ShopPoExample example = new ShopPoExample();
        ShopPoExample.Criteria criteria = example.createCriteria();
        List<ShopPo> ShopPos = null;
        try {
            ShopPos = shopMapper.selectByExample(example);
        } catch (DataAccessException e) {
            logger.error("getAllShops: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        //ReturnObject<List> retObj ;
        List<Shop> ShopSimpleList = new ArrayList<>(ShopPos.size());
        //System.out.println(ShopPos.size());
        if (ShopPos.size()==0) {
            logger.error("getShopStates: 数据库不存在任何店铺 ");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        for (ShopPo po : ShopPos) {
            if(po.getState()!=4){   //未被逻辑删除
            Shop shop = new Shop(po);
            logger.info("getAllShops: " + shop);
            ShopSimpleRetVo nowVo = new ShopSimpleRetVo(shop);
            ShopSimpleList.add(shop);}
        }
        return new ReturnObject<>(ShopSimpleList);
    }
/*
        List<VoObject> ret = new ArrayList<>(ShopPos.size());
        for (ShopPo po : ShopPos) {
            Shop sh = new Shop(po);
                ret.add(sh);

        }
        return new ReturnObject<>(ret);*/



    /**
     * 增加店铺
     * @param shop
     * @return
     */

    public ReturnObject<Shop> insertShop(Shop shop) {
        ShopPo shopPo = shop.gotShopPo();
        ReturnObject<Shop> retObj = null;
        try {
            int ret = shopMapper.insertSelective(shopPo);
            if (ret == 0) {
                //插入失败
                logger.debug("insertShop: insert shop fail" + shopPo.toString());
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("新增失败：" + shopPo.getName()));
            } else {
                //插入成功
                logger.debug("insertShop: insert shop = "+ shopPo.toString());
                shop.setId(shopPo.getId());
                retObj = new ReturnObject<>(shop);
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
     * 修改店铺
     * @param id 店铺 id
     * @param shopVo ShopEditVo 对象
     * @return
     */
    public ReturnObject<Object> modifyShopByVo(Long id, ShopVo shopVo) {
        // 查询密码等资料以计算新签名
        ShopPo orig=new ShopPo();
        try {
            orig = shopMapper.selectByPrimaryKey(id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // 不修改已被逻辑废弃的账户
        if (orig == null ||orig.getState()==3) {
            logger.info("用户不存在或已被删除：id = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        else if(orig.getState()!=0||orig.getState()!=1)
        {
            logger.info("店铺当前状态无法被修改：id = " + id);
            return new ReturnObject<>(ResponseCode.SHOP_STATENOTALLOW);
        }

        // 构造 User 对象以计算签名
        Shop shop = new Shop(orig);
        ShopPo po = shop.createUpdatePo(shopVo);

        // 更新数据库
        ReturnObject<Object> retObj;
        try { shopMapper.updateByPrimaryKeySelective(po);
        } catch (DataAccessException e) {
            // 如果发生 Exception，判断是邮箱还是啥重复错误
                // 其他情况属未知错误
                logger.error("数据库错误：" + e.getMessage());
                retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                        String.format("发生了严重的数据库错误：%s", e.getMessage()));
            return retObj;
        } catch (Exception e) {
            // 其他 Exception 即属未知错误
            logger.error("严重错误：" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的未知错误：%s", e.getMessage()));
        }
            logger.info("用户 id = " + id + " 的资料已更新");
            retObj = new ReturnObject<>(ResponseCode.OK);
        return retObj;
    }

    /**
     * 删除店铺
     * @param id
     * @return
     */
    public ReturnObject deleteShop(Long id) {
        ShopPo po=new ShopPo();
        ReturnObject<Object> retObj = null;
        ShopPoExample shopPo = new  ShopPoExample();
        ShopPoExample.Criteria criteria = shopPo.createCriteria();
        criteria.andIdEqualTo(id);
        try {
           po = shopMapper.selectByPrimaryKey(id);
        } catch (DataAccessException e) {
            logger.error("getShop id= ",id," : DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if(po==null||po.getState()==3)
        {
            logger.info("商铺不存在或已被删除：id = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        else if(po.getState()!=1||po.getState()!=2)
        {
            logger.info("店铺当前状态无法被关闭：id = " + id);
            return new ReturnObject<>(ResponseCode.SHOP_STATENOTALLOW);
        }

        int ret;
        Byte state=3;
        ShopPo newPo=new ShopPo();
        newPo.setState(state);
        newPo.setId(id);
        newPo.setName(po.getName());
        newPo.setGmtCreate(po.getGmtCreate());
        newPo.setGmtModified(LocalDateTime.now());


        /*进行物理删除*/
        /*
        if(po.getState()==0)
        {
            logger.info("Physically delete shop: id= "+id);
            try {
                ret=shopMapper.deleteByPrimaryKey(id);
                    logger.info("店铺 id = " + id + " 已被物理删除 ");
                    retObj = new ReturnObject<>();
            }
            catch (DataAccessException e) {
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
        }
*/
        /*进行逻辑删除*/
       // else{
            logger.info("logically delete shop: id= "+id);
            try {
                ret=shopMapper.updateByPrimaryKeySelective(newPo);
                    logger.info("店铺 id = " + id + " 的状态修改为 " + newPo.getState());
                    retObj = new ReturnObject<>();
            }
            catch (DataAccessException e) {
                // 数据库错误
                logger.error("数据库错误：" + e.getMessage());
                retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                        String.format("发生了严重的数据库错误：%s", e.getMessage()));
                return retObj;
            } catch (Exception e) {
                // 属未知错误
                logger.error("严重错误：" + e.getMessage());
                retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                        String.format("发生了严重的未知错误：%s", e.getMessage()));
                return retObj;
            }
        //级联删除,若是当前店铺下有相应spu时一并删除
        List<Long> spuiId = spuDao.getAllSpuIdByShopId(id).getData();
        if(spuiId!=null){
            for(Long i : spuiId) {
                spuDao.deleteGoodsSpu(id,i);
            }
        }
            retObj=new ReturnObject<>(ResponseCode.OK);
      //  }
        return retObj;
    }

    /**
     * 管理员审核店铺
     * @param id
     * @return
     */
    public ReturnObject auditShopInfo(Long id,Boolean conJudge) {
        ShopPo po=new ShopPo();
        ReturnObject<Object> retObj = null;
        ShopPoExample shopPo = new  ShopPoExample();
        ShopPoExample.Criteria criteria = shopPo.createCriteria();
        criteria.andIdEqualTo(id);
        try {
            po = shopMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("getShop id= ",id," : DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if(po==null||po.getState()==3)
        {
            logger.info("店铺不存在或已被删除：id = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        else if(po.getState()!=0)
        {
            logger.info("店铺不处于待审核状态：id = " + id);
            return new ReturnObject<>(ResponseCode.SHOP_STATENOTALLOW);
        }

        int ret;
        Byte state;
        if(conJudge) state=2;//通过审核进行上线
        else state=4;
        ShopPo newPo=new ShopPo();
        newPo.setState(state);
        newPo.setId(id);
        newPo.setName(po.getName());
        newPo.setGmtCreate(po.getGmtCreate());
        newPo.setGmtModified(LocalDateTime.now());
        logger.info("audit shop: id= "+id);
        try {
            ret=shopMapper.updateByPrimaryKeySelective(newPo);
            if(conJudge)
            logger.info("店铺通过审核,店铺 id = " + id + " 的状态修改为 " + Shop.StateType.getTypeByCode(newPo.getState()).getDescription());
            else
                logger.info("店铺未通过审核，店铺 id = " + id + " 的状态修改为 " + Shop.StateType.getTypeByCode(newPo.getState()).getDescription());
            retObj = new ReturnObject<>(ResponseCode.OK);
        }
        catch (DataAccessException e) {
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
     * 上线店铺
     * @param id
     * @return
     */
    public ReturnObject onshelvesShop(Long id) {
        ShopPo po=new ShopPo();
        ReturnObject<Object> retObj = null;
        ShopPoExample shopPo = new  ShopPoExample();
        ShopPoExample.Criteria criteria = shopPo.createCriteria();
        criteria.andIdEqualTo(id);
        try {
            po = shopMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("getShop id= ",id," : DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if(po==null||po.getState()==3)
        {
            logger.info("店铺不存在或已被删除：id = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        else if(po.getState()!=1)
        {
            logger.info("店铺不能被上架（未处于下架状态中）：id = " + id);
            return new ReturnObject<>(ResponseCode.USER_HASSHOP);
        }

        int ret;
        Byte state=2;
        ShopPo newPo=new ShopPo();
        newPo.setState(state);
        newPo.setId(id);
        newPo.setName(po.getName());
        newPo.setGmtCreate(po.getGmtCreate());
        newPo.setGmtModified(LocalDateTime.now());
        logger.info("onshelves shop: id= "+id);
        try {
            ret=shopMapper.updateByPrimaryKeySelective(newPo);
                logger.info("店铺 id = " + id + " 的状态修改为 " + Shop.StateType.getTypeByCode(newPo.getState()).getDescription());
                retObj = new ReturnObject<>(ResponseCode.OK);
        }
        catch (DataAccessException e) {
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
     * 下线店铺
     * @param id
     * @return
     */
    public ReturnObject offshelvesShop(Long id) {
        ShopPo po=new ShopPo();
        ReturnObject<Object> retObj = null;
        ShopPoExample shopPo = new  ShopPoExample();
        ShopPoExample.Criteria criteria = shopPo.createCriteria();
        criteria.andIdEqualTo(id);
        try {
            po = shopMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("getShop id= ",id," : DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if(po==null||po.getState()==3)
        {
            logger.info("店铺不存在或已被删除：id = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        else if(po.getState()!=2)
        {
            logger.info("店铺不能被下架（未处于上架状态中）：id = " + id);
            return new ReturnObject<>(ResponseCode.USER_HASSHOP);
        }

        int ret;
        Byte state=1;
        ShopPo newPo=new ShopPo();
        newPo.setState(state);
        newPo.setId(id);
        newPo.setName(po.getName());
        newPo.setGmtCreate(po.getGmtCreate());
        newPo.setGmtModified(LocalDateTime.now());
        logger.info("offshelves shop: id= "+id);
        try {
            ret=shopMapper.updateByPrimaryKeySelective(newPo);
            logger.info("店铺 id = " + id + " 的状态修改为 " + Shop.StateType.getTypeByCode(newPo.getState()).getDescription());
            retObj = new ReturnObject<>(ResponseCode.OK);
        }
        catch (DataAccessException e) {
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

}
