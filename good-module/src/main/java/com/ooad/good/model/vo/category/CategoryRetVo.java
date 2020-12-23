package com.ooad.good.model.vo.category;

import com.ooad.good.model.bo.Category;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品类目返回视图
 */
@Data
public class CategoryRetVo {
    private Long id;
    private String name;
    private Long pid;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    /**
     * bo对象构建Vo对象
    * @param category
     */
    public CategoryRetVo(Category category){
        this.id=category.getId();
        this.name=category.getName();
        this.pid=category.getPid();
        this.gmtCreate=category.getGmtCreate();
        this.gmtModified=category.getGmtModified();

    }

}
