package com.ooad.good.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import com.ooad.good.model.po.GoodsCategoryPo;
import com.ooad.good.model.vo.category.CategoryRetVo;
import com.ooad.good.model.vo.category.CategorySimpleRetVo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Category implements VoObject {

    private Long id;
    private String name;
    private Long pid;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public  Category(){

    }

    /**
     * po构造bo对象
    * @param po
     */
    public  Category(GoodsCategoryPo po){
        this.id=po.getId();
        this.name=po.getName();
        this.pid=po.getPid();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();

    }

    /**
     * bo创建po对象
     * @return
     */
    public GoodsCategoryPo gotCategoryPo(){
        GoodsCategoryPo po=new GoodsCategoryPo();
        po.setId(this.getId());
        po.setPid(this.getPid());
        po.setName(this.getName());
        po.setGmtCreate(this.getGmtCreate());
        po.setGmtModified(this.getGmtModified());
        return po;

    }

    /**
     * 生成CategoryRetVo对象作为返回前端
     * @return
     */
    @Override
    public Object createVo(){return new CategoryRetVo(this);}

    /**
     * 生成CategorySimpleRetVo对象作为返回前端
     * @return
     */
    @Override
    public Object createSimpleVo(){return new CategorySimpleRetVo(this); }
}
