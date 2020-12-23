package com.ooad.good.model.vo.sku;

import com.ooad.good.model.bo.Sku;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Sku传值对象
 */
@Data
public class SkuVo {


    @NotNull(message = "skuSn不能为空")
    private String skuSn;

    @NotNull(message = "sku名不能为空")
    private String name;

    @NotNull(message = "sku原价不能为空")
    private Long originalPrice;

    @NotNull(message = "sku配置不能为空")
    private String configuration;

    @NotNull(message = "sku重量不能为空")
    private Long weight;

    @NotNull(message = "sku图片不能为空")
    private String imageUrl;

    @NotNull(message = "sku库存不能为空")
    private Integer inventory;

    @NotNull(message = "sku详情不能为空")
    private String detail;

    /**
     * vo创建bo对象
     * @return
     */
    public Sku createSku(){
        Sku sku=new Sku();

        sku.setSkuSn(this.skuSn);
        sku.setName(this.name);
        sku.setOriginalPrice(this.originalPrice);
        sku.setConfiguration(this.configuration);
        sku.setWeight(this.weight);
        sku.setImageUrl(this.imageUrl);
        sku.setInventory(this.inventory);
        sku.setDetail(this.detail);

        return sku;

    }

}
