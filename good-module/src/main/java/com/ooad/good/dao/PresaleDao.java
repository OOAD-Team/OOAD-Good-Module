package com.ooad.good.dao;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.goods.model.GoodsDetailDTO;
import cn.edu.xmu.oomall.goods.model.PresaleDTO;
import cn.edu.xmu.oomall.goods.model.SimpleGoodsSkuDTO;
import cn.edu.xmu.oomall.goods.model.SimpleShopDTO;
import com.ooad.good.mapper.PresaleActivityPoMapper;
import com.ooad.good.model.bo.ActivityStatus;
import com.ooad.good.model.bo.Presale;
import com.ooad.good.model.po.PresaleActivityPo;
import com.ooad.good.model.po.PresaleActivityPoExample;
import com.ooad.good.model.vo.presale.PresaleVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
public class PresaleDao {
    private  static  final Logger logger = LoggerFactory.getLogger(PresaleDao.class);

    @Autowired
    private PresaleActivityPoMapper presaleActivityPoMapper;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 查询预售活动
     * @param shopId
     * @param skuId
     * @param state
     * @param timeline
     * @param isadmin
     * @return
     */
    public ReturnObject<List<PresaleActivityPo>> findPresales(Long shopId, Long skuId, Integer state, Integer timeline, boolean isadmin) {

        PresaleActivityPoExample example = new PresaleActivityPoExample();
        PresaleActivityPoExample.Criteria criteria = example.createCriteria();

        //按店铺id查询
        if(shopId!=null && shopId!=-1L && shopId!=0L)
            criteria.andShopIdEqualTo(shopId);
        //按sku_id查询
        if(skuId!=null)
            criteria.andGoodsSkuIdEqualTo(skuId);
        //按状态查询
        List<PresaleActivityPo> results = null;
        if(state!=null) {
            if(state == 0)
                criteria.andStateEqualTo(ActivityStatus.OFF_SHELVES.getCode().byteValue());
            else if(state == 1)
                criteria.andStateEqualTo(ActivityStatus.ON_SHELVES.getCode().byteValue());
            else if(state == 2)
                criteria.andStateEqualTo(ActivityStatus.DELETED.getCode().byteValue());
            else
                criteria.andStateEqualTo(Integer.valueOf(4).byteValue());

        }
        //按timeline查询
        if(timeline!=null)
        {
            switch (timeline){
                case 0:
                    criteria.andBeginTimeGreaterThan(LocalDateTime.now());
                    break;
                case 1:
                    LocalDateTime searchTime= LocalDateTime.now();
                    searchTime=searchTime.plusDays(2);
                    searchTime=searchTime.minusHours(searchTime.getHour());
                    searchTime=searchTime.minusMinutes(searchTime.getMinute());
                    searchTime=searchTime.minusSeconds(searchTime.getSecond());
                    searchTime=searchTime.minusNanos(searchTime.getNano());
                    LocalDateTime searchTimeMax=searchTime;//时间段上限
                    LocalDateTime searchTimeMin=searchTime.minusDays(1);//时间段下限
                    criteria.andBeginTimeGreaterThanOrEqualTo(searchTimeMin);//beginTime>=明日零点
                    criteria.andBeginTimeLessThan(searchTimeMax);//beginTime<后日零点
                    break;
                case 2:
                    criteria.andBeginTimeLessThanOrEqualTo(LocalDateTime.now());
                    criteria.andEndTimeGreaterThanOrEqualTo(LocalDateTime.now());
                    break;
                case 3:
                    criteria.andEndTimeLessThan(LocalDateTime.now());

            }
        }


        //不是管理员则只显示有效的活动
        if(!isadmin) {
            criteria.andStateEqualTo(ActivityStatus.ON_SHELVES.getCode().byteValue());

        }

        try {
            results = presaleActivityPoMapper.selectByExample(example);
        } catch (Exception e) {
            StringBuilder message = new StringBuilder().append("queryPresales: ").append(e.getMessage());
            logger.error(message.toString());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }

        return new ReturnObject<>(results);

    }



    /**
     * 管理员新增sku预售活动
     * @param shopId
     * @param id
     * @param presaleVo
     * @param simpleGoodsSkuDTO
     * @param simpleShopDTO
     * @return
     */
    public ReturnObject addSkuPresale(Long shopId, Long id,
                                           PresaleVo presaleVo,
                                           SimpleGoodsSkuDTO simpleGoodsSkuDTO,
                                           SimpleShopDTO simpleShopDTO) {


        //1. 此sku是否正在参加其他预售
        if(checkInPresale(id,presaleVo.getBeginTime(),presaleVo.getEndTime()).getData()){
            logger.debug("此sku正在参加其他预售");
            return new ReturnObject(ResponseCode.PRESALE_STATENOTALLOW);
        }
        PresaleActivityPo presaleActivityPo = new PresaleActivityPo();
        presaleActivityPo.setShopId(shopId);
        presaleActivityPo.setId(id);

        presaleActivityPo.setAdvancePayPrice(presaleVo.getAdvancePayPrice());
        presaleActivityPo.setRestPayPrice(presaleVo.getRestPayPrice());
        presaleActivityPo.setQuantity(presaleVo.getQuantity());
        presaleActivityPo.setState(ActivityStatus.OFF_SHELVES.getCode().byteValue());
        presaleActivityPo.setBeginTime(presaleVo.getBeginTime());
        presaleActivityPo.setEndTime(presaleVo.getEndTime());
        presaleActivityPo.setPayTime(presaleVo.getPayTime());

        try {
            presaleActivityPoMapper.insertSelective(presaleActivityPo);
        } catch (Exception e) {
            StringBuilder message = new StringBuilder().append("addSkuPresale: ").append(e.getMessage());
            logger.error(message.toString());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        Presale presale = new Presale(presaleActivityPo,simpleGoodsSkuDTO,simpleShopDTO);
        return new ReturnObject<>(presale);
    }

    /**
     * 查询sku是否在某个预售活动中
     * @param id
     * @param beginTime
     * @param endTime
     * @return
     */
    public ReturnObject<Boolean> checkInPresale(Long id,LocalDateTime beginTime,LocalDateTime endTime){
        PresaleActivityPoExample example = new PresaleActivityPoExample();
        PresaleActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSkuIdEqualTo(id);
        List<PresaleActivityPo> list = null;
        try {
            list = presaleActivityPoMapper.selectByExample(example);
        } catch (Exception e) {
            StringBuilder message = new StringBuilder().append("checkInPresale: ").append(e.getMessage());
            logger.error(message.toString());
        }

        //数据库中的活动开始时间晚于endTime，或结束时间早于beginTime，才返回false
        if(!list.isEmpty())
        {
            for(PresaleActivityPo p : list){
                if(!(p.getBeginTime().isAfter(endTime)||p.getEndTime().isBefore(beginTime)))
                    return new ReturnObject<>(true);
            }
        }
        return new ReturnObject<>(false);
    }

    /**
     * 管理员修改预售活动
     * @param shopId
     * @param id
     * @param presaleVo
     * @return
     */
    public ReturnObject modifyPresaleOfSKU(Long shopId, Long id, PresaleVo presaleVo) {

        //1.查询此presale
        PresaleActivityPo oldPo = null;
        try {
            oldPo = presaleActivityPoMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            StringBuilder message = new StringBuilder().append("modifyPresaleofSPU:select: ").append(e.getMessage());
            logger.error(message.toString());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if(oldPo == null)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);

        //2.若shopId不一致，则无权限访问
        if(oldPo.getShopId()!= shopId)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);

        //3.若状态不为下线，则团购活动禁止
        if(oldPo.getState()!=ActivityStatus.OFF_SHELVES.getCode().byteValue())
            return new ReturnObject<>(ResponseCode.PRESALE_STATENOTALLOW);

        //4.修改数据库
        PresaleActivityPo presaleActivityPo = new PresaleActivityPo();
        presaleActivityPo.setShopId(shopId);
        presaleActivityPo.setId(id);
        presaleActivityPo.setAdvancePayPrice(presaleVo.getAdvancePayPrice());
        presaleActivityPo.setRestPayPrice(presaleVo.getRestPayPrice());
        presaleActivityPo.setQuantity(presaleVo.getQuantity());
        presaleActivityPo.setState(ActivityStatus.OFF_SHELVES.getCode().byteValue());



        presaleActivityPo.setBeginTime(presaleVo.getBeginTime());
        presaleActivityPo.setEndTime(presaleVo.getEndTime());
        presaleActivityPo.setPayTime(presaleVo.getPayTime());

        try {
            presaleActivityPoMapper.updateByPrimaryKeySelective(presaleActivityPo);
        } catch (Exception e) {
            StringBuilder message = new StringBuilder().append("modifyPresaleofSPU:update: ").append(e.getMessage());
            logger.error(message.toString());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }

        //5.返回
        return new ReturnObject<>(ResponseCode.OK);
    }

    /**
     * 管理员逻辑删除预售活动
     * @param shopId
     * @param id
     * @return
     */
    public ReturnObject cancelPresaleOfSKU(Long shopId, Long id) {

        //1.查询此presale
        PresaleActivityPo oldPo = null;
        try {
            oldPo = presaleActivityPoMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            StringBuilder message = new StringBuilder().append("modifyPresaleofSPU:select: ").append(e.getMessage());
            logger.error(message.toString());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }

        //2.若shopId不一致，则无权限访问
        if(oldPo.getShopId()!= shopId)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);

        //3.若状态不为下线，则团购活动禁止
        if(oldPo.getState()!=ActivityStatus.OFF_SHELVES.getCode().byteValue())
            return new ReturnObject<>(ResponseCode.PRESALE_STATENOTALLOW);

        //4.修改状态
        PresaleActivityPo newPresalePo = new PresaleActivityPo();
        newPresalePo.setId(id);
        newPresalePo.setState(ActivityStatus.DELETED.getCode().byteValue());
        try {
            presaleActivityPoMapper.updateByPrimaryKeySelective(newPresalePo);
        } catch (Exception e) {
            StringBuilder message = new StringBuilder().append("cancelPresaleofSPU: ").append(e.getMessage());
            logger.error(message.toString());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }

        //5.从redis中删除
        if(delelePresaleInventoryFromRedis(id).getCode()!=ResponseCode.OK)
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        if(delelePresalePriceFromRedis(id).getCode()!=ResponseCode.OK)
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);

        //6.返回
        return new ReturnObject<>(ResponseCode.OK);
    }

    /**
     * 管理员上线预售活动
     * @param shopId
     * @param id
     * @return
     */
    public ReturnObject putPresaleOnShelves(Long shopId, Long id) {


        //1.查询此presale
        PresaleActivityPo presaleActivityPo = null;
        try {
            presaleActivityPo = presaleActivityPoMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            StringBuilder message = new StringBuilder().append("putGrouponOnShelves: ").append(e.getMessage());
            logger.error(message.toString());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if(presaleActivityPo == null)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);

        //2.若shopId不一致，则无权限访问
        if(presaleActivityPo.getShopId()!= shopId)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);

        //3.若状态已删除，或已上线，则预售活动禁止
        if(presaleActivityPo.getState() == ActivityStatus.DELETED.getCode().byteValue()
                ||presaleActivityPo.getState() == ActivityStatus.ON_SHELVES.getCode().byteValue()){
            return new ReturnObject<>(ResponseCode.PRESALE_STATENOTALLOW);
        }

        //4.若时间已过期，则无效，预售活动禁止
        if(presaleActivityPo.getBeginTime().isBefore(LocalDateTime.now())||
                presaleActivityPo.getEndTime().isBefore(LocalDateTime.now())) {
            return new ReturnObject<>(ResponseCode.GROUPON_STATENOTALLOW);
        }

        //5.修改状态
        PresaleActivityPo newPresalePo = new PresaleActivityPo();
        newPresalePo.setId(id);
        newPresalePo.setState(ActivityStatus.ON_SHELVES.getCode().byteValue());
        try {
            presaleActivityPoMapper.updateByPrimaryKeySelective(newPresalePo);
        } catch (Exception e) {
            StringBuilder message = new StringBuilder().append("putPresaleOnShelves: ").append(e.getMessage());
            logger.error(message.toString());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }

        //6.返回
        return new ReturnObject<>(ResponseCode.OK);
    }

    /**
     * 管理员下线预售活动
     * @param shopId
     * @param id
     * @return
     */
    public ReturnObject putPresaleOffShelves(Long shopId, Long id) {

        //1.查询此presale
        PresaleActivityPo presaleActivityPo = null;
        try {
            presaleActivityPo = presaleActivityPoMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            StringBuilder message = new StringBuilder().append("putGrouponOnShelves: ").append(e.getMessage());
            logger.error(message.toString());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if(presaleActivityPo == null)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);

        //2.若shopId不一致，则无权限访问
        if(presaleActivityPo.getShopId()!= shopId)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);

        //3.若状态已删除，或已下线，则预售活动禁止
        if(presaleActivityPo.getState() == ActivityStatus.DELETED.getCode().byteValue()
                ||presaleActivityPo.getState() == ActivityStatus.OFF_SHELVES.getCode().byteValue()){
            return new ReturnObject<>(ResponseCode.PRESALE_STATENOTALLOW);
        }

        //4.若时间已过期，则无效，预售活动禁止
        if(presaleActivityPo.getBeginTime().isBefore(LocalDateTime.now())||
                presaleActivityPo.getEndTime().isBefore(LocalDateTime.now())) {
            return new ReturnObject<>(ResponseCode.GROUPON_STATENOTALLOW);
        }

        //5.修改状态
        PresaleActivityPo newPresalePo = new PresaleActivityPo();
        newPresalePo.setId(id);
        newPresalePo.setState(ActivityStatus.OFF_SHELVES.getCode().byteValue());
        try {
            presaleActivityPoMapper.updateByPrimaryKeySelective(newPresalePo);
        } catch (Exception e) {
            StringBuilder message = new StringBuilder().append("putPresaleOnShelves: ").append(e.getMessage());
            logger.error(message.toString());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }

        //6.从redis中删除
        if(delelePresaleInventoryFromRedis(id).getCode()!=ResponseCode.OK)
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        if(delelePresalePriceFromRedis(id).getCode()!=ResponseCode.OK)
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);

        //7.返回
        return new ReturnObject<>(ResponseCode.OK);

    }


    /**
     * 判断预售活动是否有效
     * @param presaleId
     * @return
     */
    public ReturnObject<PresaleDTO> judgePresaleValid(Long presaleId) {
        //1.查找是否有此presale
        PresaleActivityPo po = null;
        try {
            po = presaleActivityPoMapper.selectByPrimaryKey(presaleId);
        } catch (Exception e) {
            StringBuilder message = new StringBuilder().append("judgePresaleValid: ").append(e.getMessage());
            logger.error(message.toString());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        //2.新建DTO
        PresaleDTO presaleDTO = new PresaleDTO();

        //3.无此id则返回false，有则校验时间
        if(po!=null && po.getBeginTime().isBefore(LocalDateTime.now())&&
                po.getPayTime().isAfter(LocalDateTime.now()) &&
                po.getState() == ActivityStatus.ON_SHELVES.getCode().byteValue()){
            presaleDTO.setIsValid(true);
            presaleDTO.setAdvancePayPrice(po.getAdvancePayPrice());
            presaleDTO.setRestPayPrice(po.getRestPayPrice());
        }
        else
            presaleDTO.setIsValid(false);

        return new ReturnObject<>(presaleDTO);


    }

    public ReturnObject<Boolean> paymentPresaleIdValid(Long presaleId) {

        //1.查找是否有此presale
        PresaleActivityPo po = null;
        try {
            po = presaleActivityPoMapper.selectByPrimaryKey(presaleId);
        } catch (Exception e) {
            StringBuilder message = new StringBuilder().append("judgePresaleValid: ").append(e.getMessage());
            logger.error(message.toString());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        //2.无此id则返回false，有则校验时间
        if(po!=null && po.getPayTime().isBefore(LocalDateTime.now())&&
                po.getEndTime().isAfter(LocalDateTime.now())&&
                po.getState() == ActivityStatus.ON_SHELVES.getCode().byteValue()){
            return new ReturnObject<>(true);
        }
        else
            return new ReturnObject<>(false);
    }


    public ReturnObject<GoodsDetailDTO> modifyPresaleInventory(Long activityId, Integer quantity) {
        String key="pa_"+activityId;
        PresaleActivityPo presaleActivityPo=new PresaleActivityPo();
        Integer inventory;
        Long price;
        GoodsDetailDTO dto=new GoodsDetailDTO();
        //没在redis里找到
        if(!redisTemplate.opsForHash().hasKey(key,"quantity")) {
            //presale存在
            presaleActivityPo = presaleActivityPoMapper.selectByPrimaryKey(activityId);
            if (presaleActivityPo == null) return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);

            //判断presale活动是否有效
            if(presaleActivityPo.getState()!=ActivityStatus.ON_SHELVES.getCode().byteValue() ||
                    presaleActivityPo.getEndTime().isBefore(LocalDateTime.now()))
                return new ReturnObject<>(ResponseCode.PRESALE_STATENOTALLOW);

            //库存足够
            inventory=presaleActivityPo.getQuantity();
            if (inventory < quantity) return new ReturnObject<>(ResponseCode.SKU_NOTENOUGH);

            //设置定金
            price=presaleActivityPo.getAdvancePayPrice();
            //添加到redis
            redisTemplate.opsForHash().put(key,"quantity",inventory-quantity);
            redisTemplate.opsForHash().put(key,"price",price);
            redisTemplate.expire(key, Duration.between(LocalDateTime.now(),presaleActivityPo.getEndTime()).toHours(), TimeUnit.HOURS);
        }
        //已经存到redis里
        else{
            inventory= (Integer) redisTemplate.opsForHash().get(key,"quantity");

            //库存足够
            if(quantity>inventory)
                return new ReturnObject<>(ResponseCode.SKU_NOTENOUGH);

            //设置定金
            price=(Long)redisTemplate.opsForHash().get(key,"price");
            //设置presaleId
            presaleActivityPo.setId(activityId);

            //更新redis
            Long seconds = redisTemplate.opsForValue().getOperations().getExpire(key);
            redisTemplate.opsForHash().delete(key,"quantity");
            redisTemplate.opsForHash().put(key,"quantity",inventory-quantity);
            redisTemplate.expire(key,Duration.ofSeconds(seconds).toHours(), TimeUnit.HOURS);
        }

        //设置dto
        dto.setInventory(inventory);
        dto.setPrice(price);

        //设置修改量
        presaleActivityPo.setQuantity(inventory-quantity);

        //修改库存
        presaleActivityPoMapper.updateByPrimaryKeySelective(presaleActivityPo);

        return new ReturnObject<>(dto);
    }


    public ReturnObject delelePresaleInventoryFromRedis(Long presaleId){
        String key="pa_"+ presaleId;
        //如果存在
        if(redisTemplate.opsForHash().hasKey(key,"quantity")) {
            redisTemplate.opsForHash().delete(key,"quantity");
        }
        return new ReturnObject<>(ResponseCode.OK);
    }

    public ReturnObject delelePresalePriceFromRedis(Long presaleId){
        String key="pa_"+ presaleId;
        //如果存在
        if(redisTemplate.opsForHash().hasKey(key,"price")) {
            redisTemplate.opsForHash().delete(key,"price");
        }
        return new ReturnObject<>(ResponseCode.OK);
    }

    public void updateByPrimaryKeySelective(PresaleActivityPo presaleActivityPo)
    {
        try{
            presaleActivityPoMapper.updateByPrimaryKeySelective(presaleActivityPo);
        }
        catch (Exception e)
        {
            logger.error("严重错误：" + e.getMessage());
        }
    }


}
