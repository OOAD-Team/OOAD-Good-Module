package com.ooad.good.model.vo.shop;

import com.ooad.good.model.bo.Shop;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ShopVo {
    @ApiModelProperty(value="店铺名称")
    @NotNull(message = "店铺名不能为空")
    private String name;

    /**
     * 构造函数
     * vo创建bo对象
     * @return
     */
    public Shop createShop(){
        Shop shop =new Shop();
        shop.setName(this.name);
        return shop;
    }

}
