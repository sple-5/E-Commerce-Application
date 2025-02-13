package com.app.payloads;

import lombok.Data;
import java.util.List;

@Data
public class CouponResponse {
    private List<CouponDTO> coupons;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean lastPage;
}
