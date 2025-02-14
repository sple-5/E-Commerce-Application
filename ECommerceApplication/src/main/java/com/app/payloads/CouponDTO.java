package com.app.payloads;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CouponDTO {
    private Long id;
    private String code;
    private BigDecimal discountAmount;
    private Integer usageLimit;
    private Integer usedCount = 0;
    private LocalDate expirationDate;
}
