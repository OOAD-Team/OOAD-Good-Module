package com.ooad.good.model.bo;

/**
 * 预售活动传值对象
 */

import cn.edu.xmu.ooad.model.VoObject;
import com.ooad.good.model.po.PresaleActivityPo;
import com.ooad.good.model.vo.BrandRetVo;
import com.ooad.good.model.vo.BrandSimpleRetVo;
import com.ooad.good.model.vo.PresaleRetVo;
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

    public Presale(PresaleActivityPo po){

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


    }
    @Override
    public Object createVo(){return null;}

    /**
     * 生成BrandSimpleRetVo对象作为返回前端
     * @return
     */
    @Override
    public BrandSimpleRetVo createSimpleVo(){return null; }
}
