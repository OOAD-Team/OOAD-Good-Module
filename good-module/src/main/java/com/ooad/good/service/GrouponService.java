package com.ooad.good.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.goods.model.GoodsSpuPoDTO;
import cn.edu.xmu.oomall.goods.model.SimpleShopDTO;
import cn.edu.xmu.oomall.goods.service.IGoodsService;
import cn.edu.xmu.oomall.order.service.IOrderService;
import com.github.pagehelper.PageInfo;
import com.ooad.good.dao.GrouponDao;
import com.ooad.good.model.vo.groupon.GrouponVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GrouponService {


    @Autowired
    GrouponDao grouponDao;

    @DubboReference(check = false)
    IGoodsService iGoodsService;

    @DubboReference(check = false)
    IOrderService iOrderService;

    private static final Logger logger = LoggerFactory.getLogger(GrouponService.class);

    public ReturnObject modifyGrouponofSPU(Long shopId, Long id, GrouponVo grouponVo) {
        //1. shopId是否存在
        SimpleShopDTO simpleShopDTO = iGoodsService.getSimpleShopByShopId(shopId).getData();
        if(simpleShopDTO == null)
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);

        return grouponDao.modifyGrouponofSPU(shopId, id, grouponVo);
    }

    public ReturnObject createGrouponofSPU(Long shopId, Long id, GrouponVo grouponVo) {

        //1. shopId是否存在
        SimpleShopDTO simpleShopDTO = iGoodsService.getSimpleShopByShopId(shopId).getData();
        if(simpleShopDTO == null)
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);

        //2. 检查是否存在spuId
        GoodsSpuPoDTO goodsSpuPoDTO = iGoodsService.getSpuBySpuId(id).getData();
        if(goodsSpuPoDTO == null)
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);

        //3.若shopId不一致，则无权限访问
        if(goodsSpuPoDTO.getShopId()!= shopId)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);

        return grouponDao.createGrouponofSPU(shopId, id, grouponVo, goodsSpuPoDTO, simpleShopDTO);

    }

    public ReturnObject putGrouponOnShelves(Long shopId, Long id) {

        //1. shopId是否存在
        SimpleShopDTO simpleShopDTO = iGoodsService.getSimpleShopByShopId(shopId).getData();
        if(simpleShopDTO == null)
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);

        return grouponDao.putGrouponOnShelves(shopId,id);

    }

    public ReturnObject putGrouponOffShelves(Long shopId, Long id) {
        //1. shopId是否存在
        SimpleShopDTO simpleShopDTO = iGoodsService.getSimpleShopByShopId(shopId).getData();
        if(simpleShopDTO == null)
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);

        //2. 调用dao
        ReturnObject retobj = grouponDao.putGrouponOffShelves(shopId,id);
        if(retobj.getCode()!=ResponseCode.OK)
            return retobj;

        //3. 如无其他错误，则通知订单模块修改订单类型
        try {
            iOrderService.putGrouponOffshelves(id);
        } catch (Exception e) {
            logger.debug("dubbo error!");
        }
        return new ReturnObject<>(ResponseCode.OK);

    }

    public ReturnObject cancelGrouponofSPU(Long shopId, Long id) {
        //1. shopId是否存在
        SimpleShopDTO simpleShopDTO = iGoodsService.getSimpleShopByShopId(shopId).getData();
        if(simpleShopDTO == null)
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);

        //2. 调用dao
        ReturnObject retobj = grouponDao.cancelGrouponofSPU(shopId,id);
        if(retobj.getCode()!=ResponseCode.OK)
            return retobj;

        //3. 如无其他错误，则通知订单模块修改订单类型
        try {
            iOrderService.putGrouponOffshelves(id);
        } catch (Exception e) {
            logger.debug("dubbo error!");
        }
        return new ReturnObject<>(ResponseCode.OK);

    }

    public ReturnObject<PageInfo<VoObject>> queryGroupons(Long shopId, Long spu_id, Integer state, Integer timeline, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pagesize, Boolean isadmin) {
        return grouponDao.queryGroupons(shopId, spu_id, state, timeline, beginTime, endTime, page, pagesize, isadmin);
    }
}
