package com.ooad.good.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import com.ooad.good.model.po.FlashSalePo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @AuthorId: 24320182203185
 * @Author: Chaoyang Deng
 * @Date: 2020/12/15 下午3:41
 */
@Data
public class FlashSale implements VoObject {
    static Byte b0=0;
    static Byte b1=1;
    static Byte b2=2;
    /**
     * 请求类型
     */
    public enum StateType {

        DELETE(b0, "已下线"),
        ONLINE(b1, "已上线"),
        OFFLINE(b2, "已删除");

        private static final Map<Byte, StateType> typeMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            typeMap = new HashMap();
            for (StateType enum1 : values()) {
                typeMap.put(enum1.code, enum1);
            }
        }

        private Byte code;
        private String description;

        StateType(Byte code, String description) {
            this.code = code;
            this.description = description;
        }


        public static StateType getTypeByCode(Byte code) {
            return typeMap.get(code);
        }

        public Byte getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public void setCode(Byte state)
        {
            this.code=state;
        }
    }

    private Long id;

    private LocalDateTime flashDate;

    private Long timeSeq;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private StateType state;

    public FlashSale(){}
    /**
     * 用bo对象创建更新po对象
     * @return
     */
    public FlashSalePo gotFlashSalePo()
    {
        FlashSalePo flashSalePo=new FlashSalePo();
        flashSalePo.setId(this.id);
        flashSalePo.setTimeSegId(this.timeSeq);
        flashSalePo.setState(this.state.getCode());
        flashSalePo.setFlashDate(this.flashDate);
        flashSalePo.setGmtCreate(this.gmtCreate);
        flashSalePo.setGmtModified(this.gmtModified);
        return flashSalePo;
    }
    /**
     * 用po对象创建更新bo对象
     * @return
     */
    public FlashSale (FlashSalePo po)
    {
        this.id=po.getId();
        this.timeSeq=po.getTimeSegId();
        this.flashDate=po.getFlashDate();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
        this.state.setCode(po.getState());
    }

    @Override
    public Object createVo() {
        return this;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
