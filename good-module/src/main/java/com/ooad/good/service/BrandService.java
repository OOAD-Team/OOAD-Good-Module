package com.ooad.good.service;


import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import com.ooad.good.dao.BrandDao;
import com.ooad.good.model.bo.Brand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BrandService {

    private Logger logger = LoggerFactory.getLogger(BrandService.class);

    @Autowired
    BrandDao brandDao;

    /**
     * 获得所有品牌
     * @param page
     * @param pageSize
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> getAllBrands(Integer page, Integer pageSize){
      return brandDao.getAllBrands(page,pageSize);

    }

    /**
     * 删除品牌
     * @param id
     * @return
     */
    @Transactional
    public ReturnObject<Object>deleteBrand(Long id){
        return brandDao.deleteBrand(id);
    }

    /**
     * 增加品牌
     * @param brand
     * @return
     */
    @Transactional
    public ReturnObject insertBrand(Brand brand) {
        ReturnObject<Brand> retObj = brandDao.insertBrand(brand);
        return retObj;
    }

    /**
     * 修改品牌
     * @param brand
     * @return
     */
    @Transactional
    public ReturnObject updateBrand(Brand brand){
        ReturnObject<Brand> retobj=brandDao.updateBrand(brand);
        return retobj;
    }

}
