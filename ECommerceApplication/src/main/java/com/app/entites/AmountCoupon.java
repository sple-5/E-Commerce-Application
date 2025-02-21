package com.app.entites;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "amount_coupons") 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AmountCoupon extends Coupon {

    @Column(nullable = false)
    private double discountAmount; 

    @Override
    public double calculateDiscount(double originalPrice) {
        return discountAmount;
    }

    @Override
    public double getDiscountAmount() {
        return discountAmount; 
    }
}
