package com.ooad.good.model.bo;

import com.ooad.good.model.po.CouponActivityPo;
import com.ooad.good.model.po.SpuPo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CouponActivity {
    private Long id;
    private String name;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private LocalDateTime couponTime;
    private Byte state;
    private Long shopId;
    private Integer quantity;
    private Byte validTerm;
    private String imageUrl;
    private String strategy;
    private Long createdBy;
    private Long modiBy;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Byte quantitiyType;

    /**
     * 构造函数
     */
    public CouponActivity(){

    }

    /**
     * po对象构建bo对象
     * @param po
     */
    public CouponActivity(CouponActivityPo po) {
        this.id=po.getId();
        this.name=po.getName();
        this.beginTime=po.getBeginTime();
        this.endTime=po.getEndTime();
        this.couponTime=po.getCouponTime();
        this.state=po.getState();
        this.shopId=po.getShopId();
        this.quantity=po.getQuantity();
        this.validTerm=po.getValidTerm();
        this.imageUrl=po.getImageUrl();
        this.strategy=po.getStrategy();
        this.createdBy=po.getCreatedBy();
        this.modiBy=po.getModiBy();
        this.gmtModified=po.getGmtModified();
        this.gmtCreate=po.getGmtCreate();
        this.quantitiyType=po.getQuantitiyType();
    }

    /**
     * 用bo对象创建更新po对象
     * @return
     */
    public CouponActivityPo gotCouponActivityPo(){

        CouponActivityPo po=new CouponActivityPo();
        po.setId(this.getId());
        po.setName(this.getName());
        po.setBeginTime(this.getBeginTime());
        po.setEndTime(this.getEndTime());
        po.setCouponTime(this.getCouponTime());
        po.setState(this.getState());
        po.setShopId(this.getShopId());
        po.setQuantity(this.getQuantity());
        po.setValidTerm(this.getValidTerm());
        po.setImageUrl(this.getImageUrl());
        po.setStrategy(this.getStrategy());
        po.setCreatedBy(this.getCreatedBy());
        po.setModiBy(this.getModiBy());
        po.setGmtCreate(this.getGmtCreate());
        po.setGmtModified(this.getGmtModified());
        po.setQuantitiyType(this.getQuantitiyType());
        return po;
    }
}
