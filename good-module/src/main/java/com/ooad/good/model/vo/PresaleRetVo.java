package com.ooad.good.model.vo;

import com.ooad.good.model.bo.Brand;
import com.ooad.good.model.bo.Presale;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PresaleRetVo {

    private Long id;

    private String name;





    /**
     * bo对象构建vo对象
     * @param presale
     */
    public PresaleRetVo(Presale presale){
        this.id=presale.getId();
        this.name=presale.getName();

    }
}
