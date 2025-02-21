package com.app.entites;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CartItem> cartItems = new ArrayList<>();

    private Double totalPrice = 0.0;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon appliedCoupon;

    public void applyCoupon(Coupon coupon) {
        if (coupon == null || !coupon.isValid()) {
            throw new IllegalArgumentException("Invalid or expired coupon");
        }

        this.appliedCoupon = coupon;
        recalculateTotal();
    }

    public void reapplyCoupon() {
        recalculateTotal();
    }

    public void recalculateTotal() {
        totalPrice = cartItems.stream().mapToDouble(CartItem::getSubtotal).sum();

        if (appliedCoupon != null) {
            double discount = appliedCoupon.calculateDiscount(totalPrice);
            totalPrice -= discount;

            if (totalPrice < 0) {
                totalPrice = 0.0;
            }
        }
    }
}
