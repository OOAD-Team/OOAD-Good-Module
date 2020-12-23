package com.ooad.good.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import com.ooad.good.model.po.FlashSaleItemPo;
import com.ooad.good.model.vo.flashSale.FlashSaleItemRetVo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @AuthorId: 24320182203185
 * @Author: Chaoyang Deng
 * @Date: 2020/12/15 下午4:06
 */
@Data
public class FlashSaleItem implements VoObject {
    private Long id;

    private Long saleId;

    private Sku GoodsSku;

    private Long price;

    private Integer quantity;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    /**
     * 用po构造bo对象
     * @param po
     */
    public FlashSaleItem (FlashSaleItemPo po)
    {
        this.id=po.getId();
        this.saleId=po.getSaleId();
        this.price=po.getPrice();
        this.quantity=po.getQuantity();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
    }

    /**
     * 用bo对象创建更新po对象
     * @return
     */
    public FlashSaleItemPo gotFlashSaleItemPo()
    {
        FlashSaleItemPo flashSaleItemPo=new FlashSaleItemPo();
        flashSaleItemPo.setId(this.id);
        flashSaleItemPo.setGoodsSkuId(this.getGoodsSku().getId());
        flashSaleItemPo.setSaleId(this.getSaleId());
        flashSaleItemPo.setPrice(this.getPrice());
        flashSaleItemPo.setQuantity(this.getQuantity());
        flashSaleItemPo.setGmtCreate(this.gmtCreate);
        flashSaleItemPo.setGmtModified(this.gmtModified);
        return flashSaleItemPo;
    }
    @Override
    public Object createVo() {
        return new FlashSaleItemRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
