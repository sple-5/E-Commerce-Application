package com.app.payloads;

import com.app.entites.Coupon;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class CouponDTO {

    private String code;
    private int usageLimit;
    private int usedCount;
    private LocalDate expirationDate;
    private double discountAmount; 
    private double discountPercentage; 
    private String discountType;

    public CouponDTO() {}

    public CouponDTO(Coupon coupon) {
        this.code = coupon.getCode();
        this.usageLimit = coupon.getUsageLimit();
        this.usedCount = coupon.getUsedCount();
        this.expirationDate = coupon.getExpirationDate();

        if (coupon instanceof com.app.entites.AmountCoupon) {
            this.discountAmount = coupon.getDiscountAmount();
            this.discountType = "AMOUNT";
        } else if (coupon instanceof com.app.entites.PercentageCoupon) {
            this.discountPercentage = coupon.getDiscountAmount(); 
            this.discountType = "PERCENTAGE";
        }
    }
}
