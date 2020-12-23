package com.ooad.good.model.bo;

/**
 * 预售活动传值对象
 */

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.oomall.goods.model.SimpleGoodsSkuDTO;
import cn.edu.xmu.oomall.goods.model.SimpleShopDTO;
import com.ooad.good.model.po.PresaleActivityPo;
import com.ooad.good.model.vo.presale.PresaleRetVo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Presale implements VoObject {


    private Long id;
    private String name;
    private LocalDateTime beginTime;
    private LocalDateTime payTime;
    private LocalDateTime endTime;
    private Byte state;
    private Long shopId;
    private Long goodsSkuId;
    private Integer quantity;
    private Long advancePayPrice;
    private Long restPayPrice;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    SimpleGoodsSkuDTO simpleGoodsSkuDTO;
    SimpleShopDTO simpleShopDTO;
    public Presale(){

    }

    /**
     * bo对象创建更新po对象
     * @return
     */
    public PresaleActivityPo gotPresalePo(){
        PresaleActivityPo po=new PresaleActivityPo();


        po.setId(this.getId());
        po.setName(this.getName());
        po.setBeginTime(this.getBeginTime());
        po.setPayTime(this.getPayTime());
        po.setEndTime(this.getEndTime());
        po.setState(this.getState());
        po.setShopId(this.getShopId());
        po.setGoodsSkuId(this.getGoodsSkuId());
        po.setQuantity(this.getQuantity());
        po.setAdvancePayPrice(this.getAdvancePayPrice());
        po.setRestPayPrice(this.getRestPayPrice());
        po.setGmtCreate(this.getGmtCreate());
        po.setGmtModified(this.getGmtModified());
        return po;
    }

    /**
     * po创建bo
     * @param po
     */
    public Presale(PresaleActivityPo po,SimpleGoodsSkuDTO simpleGoodsSkuDTO,SimpleShopDTO simpleShopDTO){
        
        this.id=po.getId();
        this.name=po.getName();
        this.beginTime=po.getBeginTime();
        this.payTime=po.getPayTime();
        this.endTime=po.getEndTime();
        this.state=po.getState();
        this.shopId=po.getShopId();
        this.goodsSkuId=po.getGoodsSkuId();
        this.quantity=po.getQuantity();
        this.advancePayPrice=po.getAdvancePayPrice();
        this.restPayPrice=po.getRestPayPrice();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();

        this.simpleGoodsSkuDTO = simpleGoodsSkuDTO;
        this.simpleShopDTO = simpleShopDTO;

    }
    @Override
    public Object createSimpleVo() {
        return null;
    }

    @Override
    public Object createVo() {

        PresaleRetVo vo = new PresaleRetVo();
        vo.setId(id);
        vo.setName(name);
        vo.setBeginTime(beginTime);
        vo.setPayTime(payTime);
        vo.setEndTime(endTime);
        vo.setGoodsSku(simpleGoodsSkuDTO);
        vo.setShop(simpleShopDTO);
        vo.setState(state);
        vo.setQuantity(quantity);
        vo.setAdvancePayPrice(advancePayPrice);
        vo.setRestPayPrice(restPayPrice);
        vo.setGmtCreate(gmtCreate);
        vo.setGmtModified(gmtModified);

        return vo;
    }
}
