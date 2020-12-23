package com.ooad.good.model.vo.spu;

import com.ooad.good.model.bo.Spu;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 商品Spus传值对象
 */
@Data
public class SpuVo {
    @NotNull(message = "spu 名不能为空")
    private String name;
    @NotNull(message = "spu 规格不能为空")
    private String spec;
    @NotNull(message = "spu 详情不能为空")
    private String detail;

    /**
     * vo构建bo
     * @return
     */
    public Spu createSpu(){
        Spu spu=new Spu();
        spu.setSpec(this.spec);
        spu.setName(this.name);
        spu.setDetail(this.detail);
        return spu;
    }
}
