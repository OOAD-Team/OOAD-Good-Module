package com.ooad.good.model.vo;

import com.ooad.good.model.bo.Brand;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 品牌传值对象
 */
@Data
public class BrandVo {
    @ApiModelProperty(value="品牌名称")
    private String name;

    @ApiModelProperty(value="品牌描述")
    private String detail;

    /**
     * 构造函数
     * vo创建bo对象
     * @return
     */
    public Brand createBrand(){
        Brand brand =new Brand();
        brand.setDetail(this.detail);
        brand.setName(this.name);
        return brand;
    }
}
