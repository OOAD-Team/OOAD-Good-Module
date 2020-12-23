package com.ooad.good.model.bo;

/**
 * 预售活动传值对象
 */

import cn.edu.xmu.ooad.model.VoObject;
import com.ooad.good.model.po.PresaleActivityPo;
import com.ooad.good.model.vo.BrandRetVo;
import com.ooad.good.model.vo.BrandSimpleRetVo;
import com.ooad.good.model.vo.PresaleRetVo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class Presale implements VoObject {

    public enum PresaleState {
        DELETE(0, "OFFLINE"),
        ONLINE(1, "ONLINE"),
        OFFLIE(2, "DELETE");


        private static final Map<Integer, PresaleState> typeMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            typeMap = new HashMap();
            for (PresaleState enum1 : values()) {
                typeMap.put(enum1.code, enum1);
            }
        }

        private int code;
        private String description;

        PresaleState(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static PresaleState getTypeByCode(Integer code) {
            return typeMap.get(code);
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

    }




    private PresaleState presaleType;
    private Long id;
    private String name;
    private LocalDateTime beginTime;
    private LocalDateTime payTime;
    private LocalDateTime endTime;
    private Byte state;
    private Long shopId;
    private Long goodsSkuId;
    private Integer quantity;
    private Long advancePayPrice;
    private Long restPayPrice;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public Presale(){

    }

    /**
     * bo对象创建更新po对象
     * @return
     */
    public PresaleActivityPo gotPresalePo(){
        PresaleActivityPo po=new PresaleActivityPo();


        po.setId(this.getId());
        po.setName(this.getName());
        po.setBeginTime(this.getBeginTime());
        po.setPayTime(this.getPayTime());
        po.setEndTime(this.getEndTime());
        po.setState(this.getState());
        po.setShopId(this.getShopId());
        po.setGoodsSkuId(this.getGoodsSkuId());
        po.setQuantity(this.getQuantity());
        po.setAdvancePayPrice(this.getAdvancePayPrice());
        po.setRestPayPrice(this.getRestPayPrice());
        po.setGmtCreate(this.getGmtCreate());
        po.setGmtModified(this.getGmtModified());
        return po;
    }

    /**
     * po创建bo
     * @param po
     */
    public Presale(PresaleActivityPo po){

        this.presaleType=PresaleState.getTypeByCode(po.getState().intValue());
        this.id=po.getId();
        this.name=po.getName();
        this.beginTime=po.getBeginTime();
        this.payTime=po.getPayTime();
        this.endTime=po.getEndTime();
        this.state=po.getState();
        this.shopId=po.getShopId();
        this.goodsSkuId=po.getGoodsSkuId();
        this.quantity=po.getQuantity();
        this.advancePayPrice=po.getAdvancePayPrice();
        this.restPayPrice=po.getRestPayPrice();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();


    }
    @Override
    public Object createVo(){return null;}

    /**
     * 生成BrandSimpleRetVo对象作为返回前端
     * @return
     */
    @Override
    public BrandSimpleRetVo createSimpleVo(){return null; }
}
