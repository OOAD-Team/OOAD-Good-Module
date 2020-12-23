package com.ooad.good.model.vo.groupon;

import com.ooad.good.model.bo.Groupon;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 团购活动传值对象
 */
@Data
public class GrouponVo {

    @NotNull(message = "团购活动规则不能为空")
    private String strategy;

    @NotNull(message = "团购活动开始时间不能为空")
    private LocalDateTime beginTime;

    @NotNull(message = "团购活动结束时间不能为空")
    private LocalDateTime endTime;


    public Groupon createGroupon(){
        Groupon groupon=new Groupon();
        groupon.setStrategy(this.strategy);
        groupon.setBeginTime(this.beginTime);
        groupon.setEndTime(this.endTime);

        return groupon;
    }
}
