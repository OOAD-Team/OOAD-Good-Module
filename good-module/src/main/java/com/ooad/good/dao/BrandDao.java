package com.ooad.good.dao;



import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.sardine.model.Privilege;

import com.ooad.good.mapper.BrandPoMapper;
import com.ooad.good.model.bo.Brand;
import com.ooad.good.model.po.BrandPo;
import com.ooad.good.model.po.BrandPoExample;
import com.ooad.good.model.vo.BrandVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class BrandDao {
    private static final Logger logger = LoggerFactory.getLogger(BrandDao.class);

    @Autowired
    private BrandPoMapper brandMapper;

    /**
     * 获得所有品牌
     *
     * @param page
     * @param pageSize
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> getAllBrands(Integer page, Integer pageSize) {
        BrandPoExample example = new BrandPoExample();
        BrandPoExample.Criteria criteria = example.createCriteria();
        PageHelper.startPage(page, pageSize);
        List<BrandPo> brandPos = null;
        try {
            brandPos = brandMapper.selectByExample(example);
        } catch (DataAccessException e) {
            logger.error("getAllBrands: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }

        List<VoObject> ret = new ArrayList<>(brandPos.size());
        for (BrandPo po : brandPos) {
            Brand brand = new Brand(po);

            logger.info("getAllBrands: " + brand);
            ret.add(brand);


        }
        PageInfo<BrandPo> brandPoPage = PageInfo.of(brandPos);
        PageInfo<VoObject> privPage = new PageInfo<>(ret);
        privPage.setPages(brandPoPage.getPages());
        privPage.setPageNum(brandPoPage.getPageNum());
        privPage.setPageSize(brandPoPage.getPageSize());
        privPage.setTotal(brandPoPage.getTotal());
        return new ReturnObject<>(privPage);
    }

    /**
     * 删除品牌
     *
     * @param id
     * @return
     */

    public ReturnObject<Object> deleteBrand(Long id) {
        ReturnObject<Object> retObj = null;
        BrandPoExample brandPo = new BrandPoExample();
        BrandPoExample.Criteria criteria = brandPo.createCriteria();
        criteria.andIdEqualTo(id);

        try {
            int ret = brandMapper.deleteByExample(brandPo);
            if (ret == 0) {
                //删除角色表
                logger.debug("deleteBrand: id not exist = " + id);
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("角色id不存在：" + id));
            }
            else {

                logger.debug("deleteRole: delete brand id = " + id);

                retObj = new ReturnObject<>();
            }

        return retObj;
    }

        catch (DataAccessException e){
            logger.error("selectAllRole: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }
    /**
     * 增加品牌
     * @param brand
     * @return
     */

    public ReturnObject<Brand> insertBrand(Brand brand) {
        BrandPo brandPo = brand.gotBrandPo();
        ReturnObject<Brand> retObj = null;
        try {
            int ret = brandMapper.insertSelective(brandPo);
            if (ret == 0) {
                //插入失败
                logger.debug("insertBrand: insert brand fail" + brandPo.toString());
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("新增失败：" + brandPo.getName()));
            } else {
                //插入成功
                logger.debug("insertBrand: insert brand = "+ brandPo.toString());
                brand.setId(brandPo.getId());
                retObj = new ReturnObject<>(brand);
            }
        } catch (DataAccessException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("auth_role.auth_role_name_uindex")) {
                //若有重复的品牌名则新增失败
                logger.debug("updateRole: have same brand name = " + brandPo.getName());
                retObj = new ReturnObject<>(ResponseCode.ROLE_REGISTERED, String.format("品牌名重复：" + brandPo.getName()));
            } else {
                // 其他数据库错误
                logger.debug("other sql exception : " + e.getMessage());
                retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
            }
        } catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 修改品牌
     * @param brand
     * @return
     */
    public ReturnObject<Brand>updateBrand(Brand brand){
        BrandPo brandPo=brand.gotBrandPo();
        ReturnObject<Brand>retObj=null;
        BrandPoExample brandPoExample=new BrandPoExample();
        BrandPoExample.Criteria criteria= brandPoExample.createCriteria();
        criteria.andIdEqualTo(brand.getId());
        try{
            int ret = brandMapper.updateByExampleSelective(brandPo, brandPoExample);
            if (ret == 0) {
                //修改失败
                logger.debug("updateRole: update brand fail : " + brandPo.toString());
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("品牌id不存在：" + brandPo.getId()));
            } else {
                //修改成功
                logger.debug("updateRole: update brand = " + brandPo.toString());
                retObj = new ReturnObject<>();
            }
        }
        catch (DataAccessException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("auth_role.auth_role_name_uindex")) {
                //若有重复的角色名则修改失败
                logger.debug("updateRole: have same brand name = " + brandPo.getName());
                retObj = new ReturnObject<>(ResponseCode.ROLE_REGISTERED, String.format("品牌名重复：" + brandPo.getName()));
            } else {
                // 其他数据库错误
                logger.debug("other sql exception : " + e.getMessage());
                retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
            }
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }
}