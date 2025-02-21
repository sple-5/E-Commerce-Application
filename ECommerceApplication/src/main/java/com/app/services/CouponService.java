package com.app.services;

import com.app.payloads.CouponDTO;
import com.app.payloads.CouponResponse;

public interface CouponService {
    
    CouponDTO createCoupon(CouponDTO couponDTO);

    CouponResponse getAllCoupons(int pageNumber, int pageSize, String sortBy, String sortOrder);

    CouponDTO getCouponByCode(String code);

    void applyCoupon(String email, Long cartId, String code);

    void cancelCoupon(String email, Long cartId);
}
