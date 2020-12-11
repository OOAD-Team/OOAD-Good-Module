package com.ooad.good.model.bo;

import com.ooad.good.model.po.BrandPo;
import com.ooad.good.model.po.Goods_categoryPo;
import com.ooad.good.model.po.Goods_spuPo;
import com.ooad.good.model.vo.BrandVo;
import com.ooad.good.model.vo.SpuVo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Spu {

    private Long id;
    private String name;
    private Long brandId;
    private Long categoryId;
    private Long freightId;
    private Long shopId;
    private String goodsSn;
    private String detail;
    private String imageUrl;
    private String spec;
    private Byte disabled;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public Spu(){

    }

    /**
     * po对象构建bo对象
     * @param po
     */
    public Spu(Goods_spuPo po) {
        this.id=po.getId();
        this.name=po.getName();
        this.brandId=po.getBrandId();
        this.categoryId=po.getCategoryId();
        this.freightId=po.getFreightId();
        this.shopId=po.getShopId();
        this.goodsSn=po.getGoodsSn();
        this.detail=po.getDetail();
        this.imageUrl=po.getImageUrl();
        this.spec=po.getSpec();
        this.disabled=po.getDisabled();
        this.gmtModified=po.getGmtModified();
        this.gmtCreate=po.getGmtCreate();

    }

    /**
     * 用vo对象创建更新po对象
     * @param vo
     * @return
     */
    public Goods_spuPo createUpdatePo(SpuVo vo){
        Goods_spuPo po=new Goods_spuPo();
        po.setId(this.getId());

        po.setName(vo.getName());
        po.setSpec(vo.getSpec());
        po.setDetail(vo.getDetail());
        po.setGmtModified(LocalDateTime.now());
        return po;
    }

    /**
     * 用bo对象创建更新po对象
     * @return
     */
    public Goods_spuPo gotSpuPo(){
        Goods_spuPo po=new Goods_spuPo();

        po.setId(this.getId());
        po.setName(this.getName());
        po.setImageUrl(this.getImageUrl());
        po.setDetail(this.getDetail());
        po.setGmtCreate(this.getGmtCreate());
        po.setGmtModified(this.getGmtModified());
        po.setBrandId(this.getBrandId());
        po.setFreightId(this.getFreightId());
        po.setCategoryId(this.getCategoryId());
        po.setGoodsSn(this.getGoodsSn());
        po.setShopId(this.getShopId());
        po.setDisabled(this.getDisabled());
        po.setSpec(this.getSpec());
        return po;
    }
}
