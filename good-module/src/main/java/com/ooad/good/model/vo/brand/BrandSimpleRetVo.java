package com.ooad.good.model.vo.brand;

import com.ooad.good.model.bo.Brand;
import lombok.Data;

/**
 * 品牌返回简单Vo
 */
@Data
public class BrandSimpleRetVo {

    private Long id;
    private String name;
    private String imageUrl;

    /**
     * bo对象构建vo对象
     * @param brand
     */
    public BrandSimpleRetVo(Brand brand){
        this.id=brand.getId();
        this.name=brand.getName();
        this.imageUrl=brand.getImageUrl();
    }
}
