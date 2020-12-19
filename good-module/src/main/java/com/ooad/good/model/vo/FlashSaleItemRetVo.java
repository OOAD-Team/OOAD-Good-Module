package com.ooad.good.model.vo;

import com.ooad.good.model.bo.FlashSale;
import com.ooad.good.model.bo.FlashSaleItem;
import com.ooad.good.model.bo.Sku;

import java.time.LocalDateTime;

/**
 * @AuthorId: 24320182203185
 * @Author: Chaoyang Deng
 * @Date: 2020/12/15 下午3:56
 */
public class FlashSaleItemRetVo {

    private Long id;

    private SkuRetVo GoodsSku;

    private Long price;

    private Integer quantity;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public FlashSaleItemRetVo(FlashSaleItem flashSaleItem)
    {
        this.id=flashSaleItem.getId();
        this.GoodsSku=new SkuRetVo(flashSaleItem.getGoodsSku());
        this.price=flashSaleItem.getPrice();
        this.quantity=flashSaleItem.getQuantity();
        this.gmtCreate=flashSaleItem.getGmtCreate();
        this.gmtModified=flashSaleItem.getGmtModified();
    }
}
