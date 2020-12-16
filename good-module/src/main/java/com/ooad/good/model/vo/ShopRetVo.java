
package com.ooad.good.model.vo;

import com.ooad.good.model.bo.Shop;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShopRetVo {
    @ApiModelProperty(value="店铺状态")
    private Byte code;
    @ApiModelProperty(value="店铺名称")
    private String name;

    public ShopRetVo(Shop shop)
    {
        this.code=shop.getState();
        this.name=shop.getName();
    }
}
