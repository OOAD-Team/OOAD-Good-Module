package com.ooad.good.dao;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;

import com.ooad.good.mapper.GoodsCategoryPoMapper;
import com.ooad.good.model.bo.Category;
import com.ooad.good.model.po.GoodsCategoryPo;
import com.ooad.good.model.po.GoodsCategoryPoExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class CategoryDao {
    private static final Logger logger = LoggerFactory.getLogger(CategoryDao.class);

    @Autowired
    private GoodsCategoryPoMapper categoryPoMapper;

    /**
     * 管理员删除商品类目
     *
     * @param id
     * @return
     */
    public ReturnObject<Object> deleteCategory(Long id) {
        ReturnObject<Object> retObj = null;
        GoodsCategoryPoExample example = new GoodsCategoryPoExample();
        GoodsCategoryPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        try {
            int ret = categoryPoMapper.deleteByExample(example);
            if (ret == 0) {

                logger.info("deleteCategory: id not exist = " + id);
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("商品类目id不存在：" + id));
            } else {
                logger.info("deleteRole: delete category id = " + id);
            }

            retObj = new ReturnObject<>();
            return retObj;
        } catch (DataAccessException e) {
            logger.error("selectAllRole: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }


    /**
     * 管理员修改商品类目信息
     * @param category
     * @return
     */
    public ReturnObject<Category> updateCategory(Category category) {
        GoodsCategoryPo categoryPo = category.gotCategoryPo();
        ReturnObject<Category> retObj = null;
        GoodsCategoryPoExample example = new GoodsCategoryPoExample();
        GoodsCategoryPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(category.getId());


        try {
            int ret = categoryPoMapper.updateByExampleSelective(categoryPo, example);
//            int ret = roleMapper.updateByPrimaryKeySelective(categoryPo);
            if (ret == 0) {
                //修改失败
                logger.debug("updateCategory: update category fail : " + categoryPo.toString());
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("商品类目id不存在：" + categoryPo.getId()));
            } else {
                //修改成功
                logger.debug("updateRole: update category = " + categoryPo.toString());
                retObj = new ReturnObject<>();
            }
        } catch (DataAccessException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("auth_role.auth_role_name_uindex")) {
                //若有重复的角色名则修改失败
                logger.debug("updateCategory: have same category name = " + categoryPo.getName());
                retObj = new ReturnObject<>(ResponseCode.ROLE_REGISTERED, String.format("商品类目名重复：" + categoryPo.getName()));
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
}