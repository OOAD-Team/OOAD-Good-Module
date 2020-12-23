package com.ooad.good.model.vo.comment;

import com.ooad.good.model.bo.Comment;
import lombok.Data;

@Data
public class CommentStateRetVo {
    private Long Code;
    private String name;
    public CommentStateRetVo(Comment.State state){
        Code=Long.valueOf(state.getCode());
        name=state.getDescription();
    }
}
