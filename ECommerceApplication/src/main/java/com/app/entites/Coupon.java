package com.app.entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    
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

    private double discountAmount; 

    public boolean isValid() {
        return usedCount < usageLimit && LocalDate.now().isBefore(expirationDate);
    }

    public void useCoupon() {
        if (isValid()) {
            usedCount++;
        } else {
            throw new IllegalStateException("Coupon is expired or usage limit reached.");
        }
    }

    public void unuseCoupon() {
        usedCount--;
    }
}
