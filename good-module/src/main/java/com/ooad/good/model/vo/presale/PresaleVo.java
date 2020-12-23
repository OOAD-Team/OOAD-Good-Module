package com.ooad.good.model.vo.presale;

import com.ooad.good.model.bo.Presale;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class PresaleVo {

    @NotNull(message = "预售活动名称不能为空")
    private String name;

    @NotNull(message = "预售活动开始时间不能为空")
    private LocalDateTime beginTime;

    @NotNull(message = "预售活动支付时间不能为空")
    private LocalDateTime payTime;

    @NotNull(message = "预售活动结束时间不能为空")
    private LocalDateTime endTime;

    @NotNull(message = "预售活动数量不能为空")
    private Integer quantity;

    @NotNull(message = "预售活动定金不能为空")
    private Long advancePayPrice;

    @NotNull(message = "预售活动尾款不能为空")
    private Long restPayPrice;

    /**
     * vo构造bo
     * @return
     */
    public Presale createPresale(){
        Presale presale=new Presale();
        presale.setName(this.name);
        presale.setBeginTime(this.beginTime);
        presale.setPayTime(this.payTime);
        presale.setEndTime(this.endTime);
        presale.setQuantity(this.quantity);
        presale.setAdvancePayPrice(this.advancePayPrice);
        presale.setRestPayPrice(this.restPayPrice);

        return presale;
    }

}
