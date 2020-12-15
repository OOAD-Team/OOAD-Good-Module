package com.ooad.good.model.vo;

import com.ooad.good.model.bo.CouponActivity;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 优惠活动传值对象
 */
@Data
public class CouponActivityVo {

    @NotNull(message = "CouponActivity 名不能为空")
    private String name;

    @NotNull(message = "CouponActivity 开始时间不能为空")
    private LocalDateTime begin_time;

    @NotNull(message = "CouponActivity 结束时间不能为空")
    private LocalDateTime end_time;

    @NotNull(message = "CouponActivity 规则不能为空")
    private String strategy;

    @NotNull(message = "CouponActivity 时间不能为空")
    private LocalDateTime coupon_time;

    @NotNull(message = "CouponActivity 数量不能为空")
    private Integer quantity;

    @NotNull(message = "")
    private Byte quantityType;

    @NotNull(message = "")
    private Byte validTerm;

    /**
     * vo创建bo
     * @return
     */
    public CouponActivity createCouponActivity(){
        CouponActivity couponActivity=new CouponActivity();

        couponActivity.setName(this.name);
        couponActivity.setBeginTime(this.begin_time);
        couponActivity.setEndTime(this.end_time);
        couponActivity.setStrategy(this.strategy);
        couponActivity.setCouponTime(this.coupon_time);
        couponActivity.setQuantity(this.quantity);
        couponActivity.setQuantitiyType(this.quantityType);
        couponActivity.setValidTerm(this.validTerm);
        return couponActivity;
    }
}
