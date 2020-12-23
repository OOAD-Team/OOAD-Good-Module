package com.ooad.good.model.vo.shop;

import com.ooad.good.model.bo.Shop;

/**
 * @AuthorId: 24320182203185
 * @Author: Chaoyang Deng
 * @Date: 2020/12/23 上午6:33
 */
public class ShopStateVo {
    //@ApiModelProperty(value="店铺状态")

    //@ApiModelProperty(value="状态名称")
    private String name;
    private Long code;

    public ShopStateVo(Shop.StateType state)
    {
        this.code=state.getCode().longValue();
        this.name=state.getDescription();
    }
}
