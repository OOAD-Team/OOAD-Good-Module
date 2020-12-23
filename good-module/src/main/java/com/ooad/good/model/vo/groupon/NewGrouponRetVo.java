package com.ooad.good.model.vo.groupon;

import cn.edu.xmu.oomall.goods.model.GoodsSpuPoDTO;
import cn.edu.xmu.oomall.goods.model.SimpleShopDTO;
import lombok.Data;


@Data
public class NewGrouponRetVo {
    Long id;
    String name;
    GoodsSpuPoDTO goodsSpu;
    SimpleShopDTO shop;
}
