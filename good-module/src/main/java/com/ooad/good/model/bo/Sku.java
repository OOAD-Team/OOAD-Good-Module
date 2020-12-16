package com.ooad.good.model.bo;

import com.ooad.good.model.po.SkuPo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Sku {

    private Long id;
    private Long goodsSpuId;
    private String skuSn;
    private String name;
    private Long originalPrice;
    private String configuration;
    private Long weight;
    private String imageUrl;
    private Integer inventory;
    private String detail;
    private Byte disabled;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Byte state;

    public Sku(){

    }

    /**
     * po创建bo
     * @param po
     */
    public Sku(SkuPo po){
        this.id=po.getId();
        this.goodsSpuId=po.getGoodsSpuId();
        this.skuSn=po.getSkuSn();
        this.name=po.getName();
        this.originalPrice=po.getOriginalPrice();
        this.configuration=po.getConfiguration();
        this.weight=po.getWeight();
        this.imageUrl=po.getImageUrl();
        this.inventory=po.getInventory();
        this.detail=po.getDetail();
        this.disabled=po.getDisabled();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
        this.state=po.getState();
    }

    /**
     * bo对象构建po对象
     * * @return
     */
    public SkuPo gotSkuPo(){

        SkuPo po=new SkuPo();
        po.setId(this.getId());
        po.setGoodsSpuId(this.getGoodsSpuId());
        po.setSkuSn(this.getSkuSn());
        po.setName(this.getName());
        po.setOriginalPrice(this.getOriginalPrice());
        po.setConfiguration(this.getConfiguration());
        po.setWeight(this.getWeight());
        po.setImageUrl(this.getImageUrl());
        po.setInventory(this.getInventory());
        po.setDetail(this.getDetail());
        po.setDisabled(this.getDisabled());
        po.setGmtCreate(this.getGmtCreate());
        po.setGmtModified(this.getGmtModified());
        po.setState(this.getState());

        return po;
    }
}
