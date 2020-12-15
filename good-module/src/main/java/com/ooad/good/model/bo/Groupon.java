package com.ooad.good.model.bo;

import com.ooad.good.model.po.GrouponActivityPo;
import com.ooad.good.model.po.PresaleActivityPo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Groupon {

    private Long id;
    private String name;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Byte state;
    private Long shopId;
    private Long goodsSpuId;
    private String strategy;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public Groupon(){

    }

    /**
     * bo对象更新创建po对象
     * @return
     */
    public GrouponActivityPo gotGrouponPo(){
        GrouponActivityPo po=new GrouponActivityPo();

        po.setId(this.getId());
        po.setName(this.getName());
        po.setBeginTime(this.getBeginTime());
        po.setEndTime(this.getEndTime());
        po.setState(this.getState());
        po.setShopId(this.getShopId());
        po.setGoodsSpuId(this.getGoodsSpuId());
        po.setStrategy(this.getStrategy());
        po.setGmtCreate(this.getGmtCreate());
        po.setGmtModified(this.getGmtModified());

        return po;
    }
}
