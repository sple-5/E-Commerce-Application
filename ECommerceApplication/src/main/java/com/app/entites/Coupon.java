package com.app.entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "coupons")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Coupon {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Coupon code is required")
    @Column(unique = true, nullable = false, length = 50)
    private String code;

    @Min(value = 1, message = "Usage limit must be at least 1")
    @Column(nullable = false)
    private Integer usageLimit;

    @Column(nullable = false)
    private Integer usedCount = 0;

    @NotNull(message = "Expiration date is required")
    @Future(message = "Expiration date must be in the future")
    private LocalDate expirationDate;

    public double getDiscountAmount() {
        return 0.0;
    }

    public double calculateDiscount(double originalPrice) {
        return 0.0;
    }

    public boolean isValid() {
        return usedCount < usageLimit && LocalDate.now().isBefore(expirationDate);
    }

    public void useCoupon() {
        if (usedCount >= usageLimit) {
            throw new IllegalStateException("Coupon has already been used the maximum number of times.");
        }
        this.usedCount++;
    }
}