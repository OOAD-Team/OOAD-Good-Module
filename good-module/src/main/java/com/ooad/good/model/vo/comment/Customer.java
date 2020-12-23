package com.ooad.good.model.vo.comment;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Customer {

    @NotNull(message = "id不得为空")
    private Long id;

    @NotNull(message = "userName不得为空")
    private String userName;

    @NotNull(message = "name不得为空")
    private String name;
}
