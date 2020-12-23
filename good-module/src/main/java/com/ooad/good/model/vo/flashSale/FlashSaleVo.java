package com.ooad.good.model.vo.flashSale;

import com.ooad.good.model.bo.FlashSale;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
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
    @Future
    @DateTimeFormat
    LocalDateTime flashDate;

    public FlashSale createFlashSale(){
        FlashSale flashSale =new FlashSale();
        flashSale.setFlashDate(this.flashDate);
        return flashSale;
    }

}
