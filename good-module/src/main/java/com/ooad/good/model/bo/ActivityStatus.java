package com.ooad.good.model.bo;

import java.util.HashMap;
import java.util.Map;

public enum ActivityStatus {
    OFF_SHELVES(0, "已下线"),
    ON_SHELVES(1, "已上线"),
    DELETED(2,"已删除");

    private static final Map<Integer, ActivityStatus> typeMap;

    static {
        typeMap = new HashMap();
        for (ActivityStatus enum1 : values()) {
            typeMap.put(enum1.code, enum1);
        }
    }

    private int code;
    private String description;


    ActivityStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
    public Integer getCode() {
        return code;
    }
    public String getDescription() {
        return description;
    }
}