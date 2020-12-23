package com.ooad.good.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import com.ooad.good.model.bo.Presale;
import com.ooad.good.model.vo.presale.PresaleVo;
import com.ooad.good.service.PresaleService;
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

@Api(value="预售服务",tags="presale")
@RestController
@RequestMapping(value = "/goods", produces = "application/json;charset=UTF-8")
public class PresaleController {

    private  static  final Logger logger = LoggerFactory.getLogger(PresaleController.class);

    @Autowired
    private PresaleService presaleService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    /**
     *  1
     * 获得预售活动的所有状态
     * @return
     */

    /**
     * 管理员新增sku预售活动
     * @param vo
     * @param bindingResult
     * @return
     */
    @Audit
    @PostMapping("/presales")
    public Object insertPresale(@Validated @RequestBody PresaleVo vo, BindingResult bindingResult) {

        logger.debug("insert presale ");
        //校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            logger.debug("validate fail");
            return returnObject;
        }

        Presale presale = vo.createPresale();
        presale.setGmtCreate(LocalDateTime.now());
        ReturnObject retObject = presaleService.insertPresale(presale);
        if (retObject.getData() != null) {
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.getRetObject(retObject);
        } else {
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }

    }

    /**
     * 管理员修改sku预售活动
     * @param id
     * @param vo
     * @param bindingResult
     * @return
     */
    @Audit
    @PutMapping("presales/{id}")
    public Object updatePresale(@PathVariable("id")Long id, @Validated @RequestBody PresaleVo vo,BindingResult bindingResult){

        logger.debug("update Presale: id ="+id);

        //校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            return returnObject;
        }

        Presale presale=vo.createPresale();
        presale.setId(id);
        presale.setGmtModified(LocalDateTime.now());

        ReturnObject retObject=presaleService.updatePresale(presale);
        if (retObject.getData() != null) {
            return Common.getRetObject(retObject);
        } else {
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }
    }

    /**
     * 查询所有有效的预售活动
     * @param page
     * @param pageSize
     * @return
     */
    @Audit
    @GetMapping("presales")
    public Object getAllPresales(@RequestParam(required = false,defaultValue = "1") Integer page,
                               @RequestParam(required = false,defaultValue = "10") Integer pageSize){

        logger.debug("getAllBrands: page = "+ page +"  pageSize ="+pageSize);

        ReturnObject<PageInfo<VoObject>> returnObject =  presaleService.getAllPresales(page, pageSize);
        return Common.getPageRetObject(returnObject);
    }

    /**
     * 管理员上线预售活动
     * @param id
     * @return
     */
    @Audit
    @PutMapping("presales/{id}/onshelves")
    public Object onlinePresale(@PathVariable("id")Long id){

        logger.debug("onlinePresale: id ="+id);

        ReturnObject retObject=presaleService.onlinePresale(id);
        return Common.getRetObject(retObject);

    }

    /**
     * 管理员下线预售活动
     * @param id
     * @return
     */
    @Audit
    @PutMapping("presales/{id}/offshelves")
    public Object offlinePresale(@PathVariable("id")Long id){

        logger.debug("offlinePresale: id ="+id);

        ReturnObject retObject=presaleService.offlinePresale(id);
        return Common.getRetObject(retObject);

    }

    /**
     * 管理员逻辑删除sku预售活动
     * @param id
     * @return
     */
    @Audit
    @DeleteMapping("presales/{id}")
    public Object deletePresale(@PathVariable("id")Long id){

        logger.debug("deletePresale: id ="+id);

        ReturnObject retObject=presaleService.deletePresale(id);
        return Common.getRetObject(retObject);

    }
    
}
