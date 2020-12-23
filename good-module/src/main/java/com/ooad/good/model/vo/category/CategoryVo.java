package com.ooad.good.model.vo.category;

import com.ooad.good.model.bo.Category;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 商品类目传值对象
 */
@Data
public class CategoryVo {
    @NotNull(message = "商品类目名不能为空")
    String name;

    /**
     * vo创建bo对象
     * @return
     */
    public Category createCategory(){
        Category category =new Category();
        category.setName(this.name);
        return category;
    }
}
