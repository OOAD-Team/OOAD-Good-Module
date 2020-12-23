package com.ooad.good.model.vo.comment;


import cn.edu.xmu.ooad.model.VoObject;
import com.ooad.good.model.bo.Comment;
import com.ooad.good.model.vo.Customer;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评论返回视图
 */
@Data

public class CommentRetVo implements VoObject {
    private Long id;
    private Customer customer;
    private Long goodsSkuId;
    private Byte type;
    private String content;
    private Byte state;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;


    public CommentRetVo(Comment comment) {
        this.id = comment.getId();
        this.goodsSkuId = comment.getGoodsSkuId();
        this.type = comment.getType();
        this.content = comment.getContent();
        this.state = comment.getState();
        this.gmtCreate = comment.getGmtCreate();
        this.gmtModified = comment.getGmtModified();
    }

    @Override
    public Object createVo() {
        return this;
    }

    @Override
    public Object createSimpleVo() {
        return this;
    }
}
