package com.ooad.good.model.bo;

/**
 * 预售活动传值对象
 */

import com.ooad.good.model.po.PresaleActivityPo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Presale {

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
}
