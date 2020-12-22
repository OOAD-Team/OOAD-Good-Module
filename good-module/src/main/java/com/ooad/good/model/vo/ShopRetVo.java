package com.ooad.good.model.vo;


import com.ooad.good.model.bo.Shop;
import io.lettuce.core.StrAlgoArgs;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShopRetVo {
    //@ApiModelProperty(value="店铺状态")
    private Long id;
    //@ApiModelProperty(value="店铺名称")
    private String name;
    private Byte state;
    private String gmtCreate;
    private String gmtModified;
}
