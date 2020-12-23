package com.ooad.good.model.vo.flashSale;

import cn.edu.xmu.oomall.goods.model.SkuInfoDTO;
import com.ooad.good.model.bo.FlashSaleItem;
import com.ooad.good.model.po.FlashSaleItemPo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @AuthorId: 24320182203185
 * @Author: Chaoyang Deng
 * @Date: 2020/12/15 下午3:56
 */
@Data
public class FlashSaleItemRetVo {

    private Long id;

    private SkuInfoDTO goodsSku;

    private Long price;

    private Integer quantity;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public FlashSaleItemRetVo(FlashSaleItemPo po) {
        this.id = po.getId();
        this.price = po.getPrice();
        this.quantity = po.getQuantity();
        this.gmtCreate = po.getGmtCreate();
        this.gmtModified = po.getGmtModified();
    }

    public FlashSaleItemRetVo(FlashSaleItem flashSaleItem)
    {
        this.id=flashSaleItem.getId();
        //this.goodsSku=new SkuRetVo(flashSaleItem.getGoodsSku());
        this.price=flashSaleItem.getPrice();
        this.quantity=flashSaleItem.getQuantity();
        this.gmtCreate=flashSaleItem.getGmtCreate();
        this.gmtModified=flashSaleItem.getGmtModified();
    }

}
