package com.ooad.good.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.order.model.OrderDTO;
import cn.edu.xmu.oomall.order.service.IOrderService;
import com.github.pagehelper.PageInfo;
import com.ooad.good.dao.CommentDao;
import com.ooad.good.model.bo.Comment;
import com.ooad.good.model.vo.comment.CommentRetVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
    private  static  final Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    private CommentDao commentDao;

    @DubboReference(check = false)
    private IOrderService iOrderService;
    /**
     * 新增sku评论
     * @param comment
     * @return
     */
    @Transactional
    public ReturnObject<CommentRetVo> insertSkuComment(Comment comment) {

        ReturnObject<OrderDTO> orderDTOReturnObject = iOrderService.getUserSelectSOrderInfo(comment.getCustomerId(), comment.getOrderitemId());


        if(orderDTOReturnObject.getData() == null)
        {
            // 记录不存在
            return new ReturnObject<>(ResponseCode.USER_NOTBUY);
        }
        else
        {
           // 记录存在
            comment.setGoodsSkuId(orderDTOReturnObject.getData().getSkuId());
        }

        return commentDao.insertSkuComment(comment);
    }

    /**
     * 查询已通过的评论
     * @param SKU_Id
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> selectAllPassComment(Long SKU_Id, Integer pageNum, Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject = commentDao.selectAllPassComment(SKU_Id, pageNum, pageSize);
        return returnObject;
    }

    /**
     * 审核评论
     * @param comment_id
     * @param conclusion
     * @return
     */
    @Transactional
    public ReturnObject<Object> auditComment(Long comment_id, boolean conclusion) {
        logger.error("service");
        return commentDao.auditComment(comment_id, conclusion);
    }

    /**
     * 买家查看自己的评论
     * @param user_Id
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> getSelfComment(Long user_Id, Integer pageNum, Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject = commentDao.getSelfComment(user_Id, pageNum, pageSize);
        return returnObject;
    }

    /**
     *管理员查看已审核/未审核评论列表
     * @param comment_state
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> showUnAuditComments(Integer comment_state, Integer pageNum, Integer pageSize) {

        ReturnObject<PageInfo<VoObject>> returnObject = commentDao.showUnAuditComments(comment_state, pageNum, pageSize);
        return returnObject;
    }
    
}
