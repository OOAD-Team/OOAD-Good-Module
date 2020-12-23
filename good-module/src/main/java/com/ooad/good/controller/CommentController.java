package com.ooad.good.controller;


import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import com.ooad.good.model.bo.Comment;
import com.ooad.good.model.vo.comment.CommentConclusionVo;
import com.ooad.good.model.vo.comment.CommentRetVo;
import com.ooad.good.model.vo.comment.CommentStateRetVo;
import com.ooad.good.model.vo.comment.CommentVo;
import com.ooad.good.service.CommentService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(value="评论服务",tags="comment")
@RestController
@RequestMapping(value = "/goods", produces = "application/json;charset=UTF-8")
public class CommentController {

    private  static  final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    /** 1
     * 获得评论的所有状态
     * @return
     */
    @GetMapping("/comments/states")
    public Object getcommentState() {
        Comment.State[] states=Comment.State.class.getEnumConstants();
        List<CommentStateRetVo> stateVos=new ArrayList<CommentStateRetVo>();
        for(int i=0;i<states.length;i++){
            stateVos.add(new CommentStateRetVo(states[i]));
        }
        return ResponseUtil.ok(new ReturnObject<List>(stateVos).getData());
    }

    /** 2
     * 买家新增sku评论
     * @param id
     * @param userId
     * @param vo
     * @param bindingResult
     * @return
     */
    @Audit
    @PostMapping("/orderitems/{id}/comments")
    @ResponseBody
    public Object insertSkuComment(@PathVariable Long id, 
                                @LoginUser @ApiIgnore @RequestParam(required = false) Long userId, 
                                @Validated @RequestBody CommentVo vo, BindingResult bindingResult){
        logger.info("insertSkuComment: commentId");

        //校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            logger.debug("validate fail");
            return returnObject;
        }
        
        Comment comment = vo.createComment();
        
        comment.setType(vo.getType());
        comment.setContent(vo.getContent());
        comment.setOrderitemId(id);
        comment.setCustomerId(userId);
        ReturnObject<CommentRetVo> commentRetVoReturnObject = commentService.insertSkuComment(comment);
        if (commentRetVoReturnObject.getData() != null) {
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.decorateReturnObject(commentRetVoReturnObject);
        } else {
            return Common.getNullRetObj(new ReturnObject<>(commentRetVoReturnObject.getCode(), commentRetVoReturnObject.getErrmsg()), httpServletResponse);
        }
    }

    /** 3
     * 查询已通过审核的评论
     * @param id
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/skus/{id}/comments")
    public Object selectAllPassComment(
            @PathVariable("id") Long id,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject =  commentService.selectAllPassComment(id, page, pageSize);

        logger.info("selectAllPassComment:"+returnObject.toString());

        if (returnObject.getData() != null) {
            return Common.getPageRetObject(returnObject);
        } else {
            return Common.getNullRetObj(new ReturnObject<>(returnObject.getCode(), returnObject.getErrmsg()), httpServletResponse);
        }
    }

    /** 4
     * 审核评论
     * @param did
     * @param id
     * @param conclusion
     * @param shopid
     * @return
     */
    @Audit // 需要认证
    @PutMapping("/shops/{did}/comments/{id}/confirm")
    public Object auditComment(@PathVariable("did") Long did, @PathVariable("id") Long id, @RequestBody CommentConclusionVo conclusion, @Depart Long shopid) {

        ReturnObject returnObject=null;
        returnObject=commentService.auditComment(id, conclusion.getConclusion());
        return Common.decorateReturnObject(returnObject);
    }

    /** 5
     * 买家查看自己的评价记录
     * @param id
     * @param page
     * @param pageSize
     * @return
     */
    @Audit
    @GetMapping("/comments")
    public Object getSelfComment(
            @LoginUser Long id,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject =  commentService.getSelfComment(id, page, pageSize);
        return Common.getPageRetObject(returnObject);
    }

    /** 6
     * 查看未审核/已审核评论列表
     * @param id
     * @param state
     * @param page
     * @param pageSize
     * @return
     */
    @Audit // 需要认证
    @GetMapping("/shops/{id}/comments/all")
    public Object showUnAuditComments(@PathVariable("id") Long id,
                                      
                                      @RequestParam(required = false, defaultValue = "2") Integer state,
                                      @RequestParam(required = false, defaultValue = "1") Integer page,
                                      @RequestParam(required = false, defaultValue = "10") Integer pageSize) {

        return commentService.showUnAuditComments(state, page, pageSize);
    }



}