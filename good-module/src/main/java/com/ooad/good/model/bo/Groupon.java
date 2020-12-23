package com.ooad.good.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import com.ooad.good.model.po.GrouponActivityPo;
import com.ooad.good.model.vo.groupon.GrouponRetVo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Groupon implements VoObject {
    Long id;
    String name;
    LocalDateTime beginTime;
    LocalDateTime endTime;

    @Override
    public GrouponRetVo createVo() {
        return new GrouponRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return new GrouponRetVo(this);
    }

    public Groupon(GrouponActivityPo po) {
        this.setId(po.getId());
        this.setBeginTime(po.getBeginTime());
        this.setEndTime(po.getEndTime());
        this.setName(po.getName());
    }
}
