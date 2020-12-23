package com.ooad.good.model.vo.presale;

import cn.edu.xmu.oomall.goods.model.SimpleGoodsSkuDTO;
import cn.edu.xmu.oomall.goods.model.SimpleShopDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PresaleRetVo {

    Long id;
    String name;
    LocalDateTime beginTime;
    LocalDateTime endTime;
    LocalDateTime payTime;
    Byte state;
    Integer quantity;
    Long advancePayPrice;
    Long restPayPrice;
    LocalDateTime gmtCreate;
    LocalDateTime gmtModified;
    SimpleGoodsSkuDTO goodsSku;
    SimpleShopDTO shop;
    
    public PresaleRetVo(){
        
    }
   
}
