package com.ooad.good.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ooad.good.controller.PresaleController;
import com.ooad.good.mapper.PresaleActivityPoMapper;
import com.ooad.good.model.bo.Brand;
import com.ooad.good.model.bo.Presale;
import com.ooad.good.model.po.BrandPo;
import com.ooad.good.model.po.BrandPoExample;
import com.ooad.good.model.po.PresaleActivityPo;
import com.ooad.good.model.po.PresaleActivityPoExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class PresaleDao {
    private  static  final Logger logger = LoggerFactory.getLogger(PresaleDao.class);

    @Autowired
    private PresaleActivityPoMapper presaleActivityPoMapper;

    /**
     * 管理员新增sku预售活动
     * @param presale
     * @return
     */
    public ReturnObject<Presale>insertPresale(Presale presale){
        PresaleActivityPo presaleActivityPo=presale.gotPresalePo();
        ReturnObject<Presale>retObj=null;
        try{
            int ret =presaleActivityPoMapper.insertSelective(presaleActivityPo);
            if (ret == 0) {
                //插入失败
                logger.debug("insertPresale: insert presale fail " + presaleActivityPo.toString());
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("新增失败：" + presaleActivityPo.getName()));
            } else {
                //插入成功
                logger.debug("insertPresale: insert presale = " + presaleActivityPo.toString());
                presale.setId(presaleActivityPo.getId());
                retObj = new ReturnObject<>(presale);
            }
        }
        catch (DataAccessException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("auth_role.auth_role_name_uindex")) {
                //若有重复的预售活动名则新增失败
                logger.debug("insertPresale: have same presale name = " + presaleActivityPo.getName());
                retObj = new ReturnObject<>(ResponseCode.ROLE_REGISTERED, String.format("角色名重复：" + presaleActivityPo.getName()));
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


    /**
     * 管理员修改sku预售活动
     * @param presale
     * @return
     */
    public ReturnObject<Presale>updatePresale(Presale presale){

        PresaleActivityPo presaleActivityPo=presale.gotPresalePo();
        ReturnObject<Presale>retObj=null;
        PresaleActivityPoExample example=new PresaleActivityPoExample();
        PresaleActivityPoExample.Criteria criteria=example.createCriteria();
        criteria.andIdEqualTo(presale.getId());

        try{
            int ret = presaleActivityPoMapper.updateByExampleSelective(presaleActivityPo, example);
//
            if (ret == 0) {
                //修改失败
                logger.debug("updatePresale: update presale fail : " + presaleActivityPo.toString());
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("预售活动id不存在：" + presaleActivityPo.getId()));
            } else {
                //修改成功
                logger.debug("updatePresale: update presale = " + presaleActivityPo.toString());
                retObj = new ReturnObject<>();
            }
        }
        catch (DataAccessException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("auth_role.auth_role_name_uindex")) {
                //若有重复的角色名则修改失败
                logger.debug("updatePresale: have same presale name = " + presaleActivityPo.getName());
                retObj = new ReturnObject<>(ResponseCode.ROLE_REGISTERED, String.format("预售活动名重复：" + presaleActivityPo.getName()));
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



    public ReturnObject<PageInfo<VoObject>> getAllPresales(Integer page, Integer pageSize) {
        PresaleActivityPoExample example = new PresaleActivityPoExample();
        PresaleActivityPoExample.Criteria criteria = example.createCriteria();

        PageHelper.startPage(page, pageSize);
        List<PresaleActivityPo> Pos = null;
        try {
           Pos = presaleActivityPoMapper.selectByExample(example);
        } catch (DataAccessException e) {
            logger.error("getAllPresales: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }

        List<VoObject> ret = new ArrayList<>(Pos.size());
        for (PresaleActivityPo po : Pos) {
            Presale presale = new Presale(po);

            logger.info("getAllBrands: " + presale);
            ret.add(presale);


        }
        PageInfo<PresaleActivityPo> brandPoPage = PageInfo.of(Pos);
        PageInfo<VoObject> privPage = new PageInfo<>(ret);
        privPage.setPages(brandPoPage.getPages());
        privPage.setPageNum(brandPoPage.getPageNum());
        privPage.setPageSize(brandPoPage.getPageSize());
        privPage.setTotal(brandPoPage.getTotal());
        return new ReturnObject<>(privPage);
    }
}
