package com.ooad.good.model.vo;

import com.ooad.good.model.bo.Sku;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SkuRetVo {
    private Long id;
    private String skuSn;
    private String name;
    private Long originalPrice;
    private String imageUrl;
    private Integer inventory;
    private Byte disabled;
    private Long price;//在哪？？？？

    public SkuRetVo(Sku sku){

        this.id=sku.getId();
        this.skuSn=sku.getSkuSn();
        this.name=sku.getName();
        this.originalPrice=sku.getOriginalPrice();
        this.imageUrl=sku.getImageUrl();
        this.inventory=sku.getInventory();
        this.disabled=sku.getDisabled();

    }
}
