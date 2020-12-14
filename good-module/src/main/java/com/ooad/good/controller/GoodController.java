package com.ooad.good.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import com.ooad.good.model.bo.Brand;
import com.ooad.good.model.bo.Category;
import com.ooad.good.model.bo.Spu;
import com.ooad.good.model.vo.BrandVo;
import com.ooad.good.model.vo.CategoryVo;
import com.ooad.good.model.vo.SpuVo;
import com.ooad.good.service.BrandService;

import com.ooad.good.service.CategoryService;
import com.ooad.good.service.SpuService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

@Api(value="商品服务",tags="goods")
@RestController
@RequestMapping(value = "/goods", produces = "application/json;charset=UTF-8")
public class GoodController {

    private  static  final Logger logger = LoggerFactory.getLogger(GoodController.class);
    @Autowired
    private HttpServletResponse httpServletResponse;
    @Autowired
    BrandService brandService;

    /**
     * 获得所有品牌
     * @param page
     * @param pageSize
     * @return
     */
    @Audit
    @GetMapping("/brands")
    public Object getAllBrands(@RequestParam(required = false,defaultValue = "1") Integer page, @RequestParam(required = false,defaultValue = "10") Integer pageSize){

        logger.debug("getAllBrands: page = "+ page +"  pageSize ="+pageSize);

        ReturnObject<PageInfo<VoObject>> returnObject =  brandService.getAllBrands(page, pageSize);
        return Common.getPageRetObject(returnObject);
    }

    /**
     * 删除品牌
     * @param id
     * @return
     */
    @Audit
    @DeleteMapping("/brands/{id}")
    public Object deleteBrand(@PathVariable("id")Long id){
        logger.debug("delete brand: id =" +id);
        ReturnObject returnObject=brandService.deleteBrand(id);
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * 增加品牌
     * @param vo
     * @param bindingResult
     * @return
     */

    @Audit
    @PostMapping("/brands")
    public Object insertBrand(@RequestBody BrandVo vo, BindingResult bindingResult){
        //校验前端数据
        Object returnObject=Common.processFieldErrors(bindingResult,httpServletResponse);
        if (null != returnObject) {
            logger.debug("validate fail");
            return returnObject;
        }
        Brand brand=vo.createBrand();
        brand.setGmtCreate(LocalDateTime.now());
        ReturnObject retObject=brandService.insertBrand(brand);
        if(retObject.getData()!=null){
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.getRetObject(retObject);
        }
        else{
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }
    }

    /**
     * 修改品牌信息
     * @param id
     * @param vo
     * @param bindingResult
     * @return
     */

    @Audit
    @PutMapping("brands/{id}")
    public Object updateBrand(@PathVariable("id")Long id,@Validated @RequestBody BrandVo vo,BindingResult bindingResult){
        logger.debug("update brand id = " + id);
        //校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            return returnObject;
        }
        Brand brand=vo.createBrand();
        brand.setId(id);
        brand.setGmtModified(LocalDateTime.now());

        ReturnObject retObject =brandService.updateBrand(brand);
        if (retObject.getData() != null) {
            return Common.getRetObject(retObject);
        } else {
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }

    }

    @Autowired
    private CategoryService categoryService;

    /**
     * 管理员删除商品类目
     * @param id
     * @return
     */
    @Audit
    @DeleteMapping("/categories/{id}")
    public Object deleteCategory(@PathVariable("id")Long id){
        logger.debug("delete Category: id = "+id);
        ReturnObject returnObject=categoryService.deleteCategory(id);
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * 管理员修改商品类目信息
     * @param id
     * @return
     */
    @Audit
    @PutMapping("/categories/{id}")
    public  Object updateCategory(@PathVariable("id")Long id, @Validated @RequestBody CategoryVo vo,BindingResult bindingResult){
        logger.debug("update Category: id = "+id);
        //校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            return returnObject;
        }
        Category category=vo.createCategory();
        category.setGmtModified(LocalDateTime.now());
        category.setId(id);

        ReturnObject retObject=categoryService.updateCategory(category);
        if (retObject.getData() != null) {
            return Common.getRetObject(retObject);
        } else {
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }
    }


    @Autowired
    private SpuService spuService;

    /**
     * 店家新建商品spu
     * @param vo
     * @param bindingResult
     * @return
     */
    @Audit
    @PostMapping("/spus")
    public Object insertSpu(@Validated @RequestBody SpuVo vo,BindingResult bindingResult){
        //校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            logger.debug("validate fail");
            return returnObject;
        }
        Spu spu=vo.createSpu();
        spu.setGmtCreate(LocalDateTime.now());
        ReturnObject retObject =spuService.insertSpu(spu);
        if (retObject.getData() != null) {
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.getRetObject(retObject);
        } else {
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }

    }

    /**
     * 店家修改spu
     * @param id
     * @param vo
     * @param bindingResult
     * @return
     */
    @Audit
    @PutMapping("/spus/{id}")
    Object updateSpu(@PathVariable("id")Long id,@Validated @RequestBody SpuVo vo,BindingResult bindingResult ){
        logger.debug("update spu: id = "+id);

        //校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            logger.debug("validate fail");
            return returnObject;
        }
        Spu spu=vo.createSpu();
        spu.setId(id);
        spu.setGmtCreate(LocalDateTime.now());

        ReturnObject retObject = spuService.updateSpu(spu);
        if (retObject.getData() != null) {
            return Common.getRetObject(retObject);
        } else {
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }
    }

}

