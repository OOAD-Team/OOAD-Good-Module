package com.ooad.good.model.vo;

import com.ooad.good.model.bo.Shop;

/**
 * @AuthorId: 24320182203185
 * @Author: Chaoyang Deng
 * @Date: 2020/12/23 上午6:33
 */
public class ShopStateVo {
    //@ApiModelProperty(value="店铺状态")
    private Long code;
    //@ApiModelProperty(value="店铺名称")
    private String name;

    public ShopStateVo(Shop shop)
    {
        this.code=shop.getState().getCode().longValue();
        this.name=shop.getState().getDescription();
    }
}
