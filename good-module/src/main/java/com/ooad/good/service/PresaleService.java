package com.ooad.good.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.goods.model.SimpleGoodsSkuDTO;
import cn.edu.xmu.oomall.goods.model.SimpleShopDTO;
import cn.edu.xmu.oomall.goods.service.IGoodsService;
import cn.edu.xmu.oomall.order.service.IOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ooad.good.dao.PresaleDao;
import com.ooad.good.model.bo.Presale;
import com.ooad.good.model.po.PresaleActivityPo;
import com.ooad.good.model.vo.presale.PresaleVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PresaleService {
    private  static  final Logger logger = LoggerFactory.getLogger(PresaleService.class);

    @Autowired
    private PresaleDao presaleDao;

    @DubboReference(check = false)
    IGoodsService iGoodsService;

    @DubboReference(check = false)
    IOrderService iOrderService;

    /**
     * 查询所有有效的预售活动
     * @param shopId
     * @param skuId
     * @param state
     * @param timeline
     * @param page
     * @param pagesize
     * @param isadmin
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> getAllValidPresales(Long shopId, Long skuId,
                                                                  Integer state, Integer timeline,
                                                                  Integer page, Integer pagesize, boolean isadmin) {

        //查询结果
        List<PresaleActivityPo> pos = null;
        ReturnObject<List<PresaleActivityPo>> returnObject = presaleDao.findPresales(shopId,skuId,state,timeline,isadmin);
        
        if(returnObject.getCode() != ResponseCode.OK) {
            return new ReturnObject<PageInfo<VoObject>>(returnObject.getCode());
        }
        pos = returnObject.getData();

        
        PageHelper.startPage(page, pagesize);
        List<VoObject> BoList = new ArrayList<>(pos.size());
        for(PresaleActivityPo po: pos)
        {
           
            SimpleGoodsSkuDTO simpleGoodsSkuDTO = iGoodsService.getSimpleSkuBySkuId(po.getGoodsSkuId()).getData();
          
            SimpleShopDTO simpleShopDTO = iGoodsService.getSimpleShopByShopId(po.getShopId()).getData();
            Presale bo = new Presale(po,simpleGoodsSkuDTO,simpleShopDTO);
            BoList.add(bo);
        }
        PageInfo<VoObject> PresalePage = PageInfo.of(BoList);
        PresalePage.setPageSize(pagesize);
        return new ReturnObject<>(PresalePage);

    }

    /**
     * 管理员查询spu所有预售活动
     * @param shopId
     * @param skuId
     * @param state
     * @param timeline
     * @return
     */
    public ReturnObject<List<Presale>> AdminQueryPresales(Long shopId, Long skuId, Integer state, Integer timeline) {

        //查询结果
        List<PresaleActivityPo> results = null;
        ReturnObject<List<PresaleActivityPo>> returnObject = presaleDao.findPresales(shopId,skuId,state,timeline,true);

        if(returnObject.getCode() != ResponseCode.OK) {
            return new ReturnObject<>(returnObject.getCode());
        }
        results = returnObject.getData();

        
        List<Presale> presales = new ArrayList<>(results.size());
        for(PresaleActivityPo po: results)
        {
            
            SimpleGoodsSkuDTO simpleGoodsSkuDTO = iGoodsService.getSimpleSkuBySkuId(po.getGoodsSkuId()).getData();
          
            SimpleShopDTO simpleShopDTO = iGoodsService.getSimpleShopByShopId(po.getShopId()).getData();
            Presale bo = new Presale(po,simpleGoodsSkuDTO,simpleShopDTO);
            presales.add(bo);
        }
        return new ReturnObject<>(presales);

    }

    /**
     * 管理员新增sku预售活动
     * @param shopId
     * @param id
     * @param presaleVo
     * @return
     */
    public ReturnObject addSkuPresale(Long shopId, Long id, PresaleVo presaleVo) {

        //1. shopId是否存在
        ReturnObject<SimpleShopDTO> returnObject = iGoodsService.getSimpleShopByShopId(shopId);
        SimpleShopDTO simpleShopDTO = returnObject.getData();

        if(simpleShopDTO == null)
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);

        //2. 检查是否存在skuId
        //TODO sku state=2 则不应该拿到
        SimpleGoodsSkuDTO simpleGoodsSkuDTO = iGoodsService.getSimpleSkuBySkuId(id).getData();
        if(simpleGoodsSkuDTO == null){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        
        //3. 此sku是否在此shop中,否则无权限操作
        if(iGoodsService.getShopIdBySkuId(id).getData()!=shopId){
            logger.debug("此shop无权限操作此sku");
            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        return presaleDao.addSkuPresale(shopId, id, presaleVo,simpleGoodsSkuDTO,simpleShopDTO);

    }


    /**
     * 管理员修改预售活动
     * @param shopId
     * @param id
     * @param presaleVo
     * @return
     */
    public ReturnObject modifyPresaleOfSKU(Long shopId, Long id, PresaleVo presaleVo) {
        //1. shopId是否存在
        ReturnObject<SimpleShopDTO> returnObject = iGoodsService.getSimpleShopByShopId(shopId);
        SimpleShopDTO simpleShopDTO = returnObject.getData();

        if(simpleShopDTO == null)
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);

        return presaleDao.modifyPresaleOfSKU(shopId,id,presaleVo);
    }

    /**
     * 管理员逻辑删除sku预售活动
     * @param shopId
     * @param id
     * @return
     */
    public ReturnObject cancelPresaleOfSKU(Long shopId, Long id) {
        //1. shopId是否存在
        ReturnObject<SimpleShopDTO> returnObject = iGoodsService.getSimpleShopByShopId(shopId);
        SimpleShopDTO simpleShopDTO = returnObject.getData();

        if(simpleShopDTO == null)
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);

        //2. 调用dao
        ReturnObject retObj = presaleDao.cancelPresaleOfSKU(shopId,id);
        if(retObj.getCode()!=ResponseCode.OK)
            return retObj;

        //3. 如无其他错误，则通知订单模块修改订单类型
        try {
            iOrderService.putPresaleOffshevles(id);
        } catch (Exception e) {
            logger.debug("dubbo error!");
        }
        return new ReturnObject<>(ResponseCode.OK);
    }

    /**
     * 管理员上线预售活动
     * @param shopId
     * @param id
     * @return
     */
    public ReturnObject putPresaleOnShelves(Long shopId, Long id) {

        //1. shopId是否存在
        ReturnObject<SimpleShopDTO> returnObject = iGoodsService.getSimpleShopByShopId(shopId);
        SimpleShopDTO simpleShopDTO = returnObject.getData();

        if(simpleShopDTO == null)
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);

        return presaleDao.putPresaleOnShelves(shopId,id);
    }

    /**
     * 管理员下线预售活动
     * @param shopId
     * @param id
     * @return
     */
    public ReturnObject putPresaleOffShelves(Long shopId, Long id) {
        //1. shopId是否存在
        ReturnObject<SimpleShopDTO> returnObject = iGoodsService.getSimpleShopByShopId(shopId);
        SimpleShopDTO simpleShopDTO = returnObject.getData();

        if(simpleShopDTO == null)
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);


        //2. 调用dao
        ReturnObject retobj = presaleDao.putPresaleOffShelves(shopId,id);
        if(retobj.getCode()!=ResponseCode.OK)
            return retobj;

        //3. 如无其他错误，则通知订单模块修改订单类型
        try {
            iOrderService.putPresaleOffshevles(id);
        } catch (Exception e) {
            logger.debug("dubbo error!");
        }
        return new ReturnObject<>(ResponseCode.OK);
    }
}
