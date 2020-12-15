package com.ooad.good.controller;

import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.ooad.good.model.bo.Groupon;
import com.ooad.good.model.bo.Presale;
import com.ooad.good.model.vo.GrouponVo;
import com.ooad.good.service.GrouponService;
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

@Api(value="团购服务",tags="groupon")
@RestController
@RequestMapping(value = "/groupon", produces = "application/json;charset=UTF-8")
public class GrouponController {
    private  static  final Logger logger = LoggerFactory.getLogger(GrouponController.class);

    @Autowired
    private GrouponService grouponService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    /**
     * 管理员对Spu新增团购活动
     * @param vo
     * @param bindingResult
     * @return
     */
    @PostMapping("/groupons")
    public Object insertGroupon(@Validated @RequestBody GrouponVo vo, BindingResult bindingResult){

        logger.debug("insert groupon ");
        //校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            logger.debug("validate fail");
            return returnObject;
        }

        Groupon groupon = vo.createGroupon();
        groupon.setGmtCreate(LocalDateTime.now());
        ReturnObject retObject = grouponService.insertGroupon(groupon);

        if (retObject.getData() != null) {
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.getRetObject(retObject);
        } else {
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }
    }

    /**
     * 管理员修改spu的团购活动
     * @param id
     * @param vo
     * @param bindingResult
     * @return
     */
    @PutMapping("/groupons/{id}")
    public Object updateGroupon(@PathVariable("id")Long id,@Validated @RequestBody GrouponVo vo,BindingResult bindingResult){
        //校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            logger.debug("validate fail");
            return returnObject;
        }

        Groupon groupon=vo.createGroupon();
        groupon.setId(id);
        groupon.setGmtModified(LocalDateTime.now());

        ReturnObject retObject=grouponService.updateGroupon(groupon);
        if (retObject.getData() != null) {
            return Common.getRetObject(retObject);
        } else {
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }
    }
}
