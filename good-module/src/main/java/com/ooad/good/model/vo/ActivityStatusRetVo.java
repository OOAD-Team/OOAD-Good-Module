package com.ooad.good.model.vo;

import com.ooad.good.model.bo.ActivityStatus;
import lombok.Data;

/**
 * 活动状态返回vo
 */
@Data
public class ActivityStatusRetVo {
    private Long code;
    private String name;

    public ActivityStatusRetVo(ActivityStatus state) {
        code=state.getCode().longValue();
        name=state.getDescription();
    }
}