package com.ooad.good.model.vo.comment;

import com.ooad.good.model.bo.Comment;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 评论传值对象
 */
@Data
public class CommentVo {

    @NotNull(message = "评论类型不能为空")
    private Byte type;

    @NotNull(message = "评论内容不能为空")
    private String content;


    /**
     * vo构造bo
     * @return
     */
    public Comment createComment()
    {
        Comment comment=new Comment();
        comment.setContent(this.content);
        comment.setType(this.type);
        return comment;
    }
}
