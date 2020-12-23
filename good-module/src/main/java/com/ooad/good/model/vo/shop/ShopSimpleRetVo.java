package com.ooad.good.model.vo.shop;

import com.ooad.good.model.bo.Shop;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShopSimpleRetVo {
    @ApiModelProperty(value="店铺id")
    private Long id;
    @ApiModelProperty(value="店铺名称")
    private String name;

    public ShopSimpleRetVo()
    {}
    public ShopSimpleRetVo(Shop shop)
    {
        this.id=shop.getId();
        this.name=shop.getName();
    }
}