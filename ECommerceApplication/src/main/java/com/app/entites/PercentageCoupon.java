    package com.app.entites;

    import jakarta.persistence.*;
    import lombok.*;

    @Entity
    @Table(name = "percentage_coupons")
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class PercentageCoupon extends Coupon {

        @Column(nullable = false)
        private double discountPercentage; 

        @Override
        public double calculateDiscount(double originalPrice) {
            return originalPrice * discountPercentage / 100;
        }

        @Override
        public double getDiscountAmount() {
            return discountPercentage;
        }
    }
