package com.ooad.good.model.vo.comment;


import com.ooad.good.model.bo.Comment;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评论返回简单视图
 */
@Data
public class CommentSimpleRetVo {

    private Long id;

    private Long customerId;

    private Long goodsSkuId;

    private Byte type;

    private String content;

    private Byte state;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    /**
     * bo构造简单返回vo
     * @param comment
     */
    public CommentSimpleRetVo(Comment comment) {
        this.id = comment.getId();
        this.customerId = comment.getCustomerId();
        this.goodsSkuId = comment.getGoodsSkuId();
        this.type = comment.getType();
        this.content = comment.getContent();
        this.state = comment.getState();
        this.gmtCreate = comment.getGmtCreate();
        this.gmtModified = comment.getGmtModified();
    }
}
