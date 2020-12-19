package com.ooad.good.model.vo;

import com.ooad.good.model.bo.FlashSale;
import com.ooad.good.model.bo.Shop;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @AuthorId: 24320182203185
 * @Author: Chaoyang Deng
 * @Date: 2020/12/15 下午3:48
 */
@Data
public class FlashSaleVo {
    @ApiModelProperty(value="秒杀日期")
    @NotNull(message = "秒杀日期不能为空")
    LocalDateTime flashDate;

    public FlashSale createFlashSale(){
        FlashSale flashSale =new FlashSale();
        flashSale.setFlashDate(this.flashDate);
        return flashSale;
    }

}
