package com.ooad.good.model.vo.brand;

import com.ooad.good.model.bo.Brand;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 品牌传值对象
 */
@Data
public class BrandVo {
    @NotNull(message = "品牌名不能为空")
    private String name;


    @NotNull(message = "品牌描述不能为空")
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
