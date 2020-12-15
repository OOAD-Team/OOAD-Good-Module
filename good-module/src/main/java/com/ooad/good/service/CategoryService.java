package com.ooad.good.service;

import cn.edu.xmu.ooad.util.ReturnObject;
import com.ooad.good.dao.BrandDao;
import com.ooad.good.dao.CategoryDao;
import com.ooad.good.model.bo.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryDao categoryDao;

    /**
     * 管理员删除商品类目
     * @param id
     * @return
     */
    @Transactional
    public  ReturnObject<Object>deleteCategory(Long id){
        return categoryDao.deleteCategory(id);
    }

    @Transactional
    public ReturnObject updateCategory(Category category){
        ReturnObject<Category>retObj=categoryDao.updateCategory(category);
        return retObj;
    }
}
