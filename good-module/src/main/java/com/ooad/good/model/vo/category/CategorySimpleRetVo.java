package com.ooad.good.model.vo.category;

import com.ooad.good.model.bo.Category;

/**
 * 商品类目简单返回视图
 */
public class CategorySimpleRetVo {
    private Long id;
    private  String name;

    public CategorySimpleRetVo(Category category){
        this.id=category.getId();
        this.name=category.getName();
    }
}
