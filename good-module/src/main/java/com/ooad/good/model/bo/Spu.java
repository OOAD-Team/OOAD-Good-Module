package com.ooad.good.model.bo;

import com.ooad.good.model.po.SpuPo;
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
    public Spu(SpuPo po) {
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
    public SpuPo createUpdatePo(SpuVo vo){
       SpuPo po=new SpuPo();
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
    public SpuPo gotSpuPo(){
        SpuPo po=new SpuPo();

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
