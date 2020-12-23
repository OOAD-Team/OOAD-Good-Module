package com.ooad.good.model.vo.brand;

import com.ooad.good.model.bo.Brand;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 品牌返回视图
 */
@Data
public class BrandRetVo {

    private Long id;

    private String name;

    private String detail;

    private String imageUrl;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;

    /**
     * bo对象构建vo对象
     * @param brand
     */
    public BrandRetVo(Brand brand){
        this.id=brand.getId();
        this.name=brand.getName();
        this.detail=brand.getDetail();
        this.imageUrl=brand.getImageUrl();
        this.gmtCreated=brand.getGmtCreate();
        this.gmtModified=brand.getGmtModified();
    }
}
