package com.ooad.good.model.vo.groupon;

import com.ooad.good.model.bo.Groupon;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GrouponRetVo {
    Long id;
    String name;
    LocalDateTime beginTime;
    LocalDateTime endTime;

    public GrouponRetVo(Groupon grouponActivity){
        this.id = grouponActivity.getId();
        this.beginTime=grouponActivity.getBeginTime();
        this.endTime=grouponActivity.getEndTime();
        this.name=grouponActivity.getName();
    }
}
