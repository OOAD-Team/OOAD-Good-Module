package com.ooad.good.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import com.ooad.good.model.po.BrandPo;
import com.ooad.good.model.vo.brand.BrandRetVo;
import com.ooad.good.model.vo.brand.BrandSimpleRetVo;
import com.ooad.good.model.vo.brand.BrandVo;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class Brand implements VoObject {

    private Long id;

    private String name;

    private String detail;

    private String imageUrl;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    /**
     * 构造函数
     */
    public Brand(){

    }
    /**
     * 用po构造bo对象
     * @param po
     */
    public Brand(BrandPo po){
        this.id=po.getId();
        this.name=po.getName();
        this.imageUrl=po.getImageUrl();
        this.detail=po.getDetail();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();

    }

    /**
     * 用vo对象创建更新po对象
     * @param vo
     * @return
     */
    public BrandPo createUpdatePo(BrandVo vo){
        BrandPo po=new BrandPo();
        po.setId(this.getId());
        po.setImageUrl(this.getImageUrl());
        po.setGmtCreate(null);
        po.setGmtModified(LocalDateTime.now());

        po.setName(vo.getName());
        po.setDetail(vo.getDetail());
        return po;
    }

    /**
     * 用bo对象创建更新po对象
     * @return
     */
    public BrandPo gotBrandPo(){
        BrandPo po=new BrandPo();
        po.setId(this.getId());
        po.setName(this.getName());
        po.setImageUrl(this.imageUrl);
        po.setDetail(this.detail);
        po.setGmtCreate(this.gmtCreate);
        po.setGmtModified(this.gmtModified);
        return po;
    }

    /**
     * 生成BrandRetVo对象作为返回前端
     * @return
     */
    @Override
    public Object createVo(){return new BrandRetVo(this);}

    /**
     * 生成BrandSimpleRetVo对象作为返回前端
     * @return
     */
    @Override
    public BrandSimpleRetVo createSimpleVo(){return new BrandSimpleRetVo(this); }
}
