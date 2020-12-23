package com.ooad.good.model.vo.groupon;


import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.oomall.goods.model.GoodsSpuPoDTO;
import cn.edu.xmu.oomall.goods.model.SimpleShopDTO;
import com.ooad.good.model.po.GrouponActivityPo;
import lombok.Data;


@Data
public class NewGroupon implements VoObject {

    Long id;
    String name;
    GoodsSpuPoDTO goodsSpu;
    SimpleShopDTO shop;
    
    public NewGroupon(GrouponActivityPo grouponActivityPo, GoodsSpuPoDTO goodsSpuPoDTO, SimpleShopDTO simpleShopDTO){
        this.id = grouponActivityPo.getId();
        this.name = grouponActivityPo.getName();
        this.goodsSpu = goodsSpuPoDTO;
        this.shop = simpleShopDTO;

    }

    @Override
    public Object createSimpleVo() {
        return null;
    }

    @Override
    public Object createVo() {
        NewGrouponRetVo vo = new NewGrouponRetVo();
        vo.setId(this.id);
        vo.setName(this.name);
        vo.setShop(this.shop);
        vo.setGoodsSpu(this.goodsSpu);
        return vo;
    }
}
