package com.app.controllers;

import com.app.config.AppConstants;
import com.app.payloads.CouponDTO;
import com.app.payloads.CouponResponse;
import com.app.services.CouponService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/admin/coupons")
    public ResponseEntity<CouponDTO> createCoupon(@RequestBody CouponDTO couponDTO) {
        CouponDTO createdCoupon = couponService.createCoupon(couponDTO);
        return new ResponseEntity<>(createdCoupon, HttpStatus.CREATED);
    }

    @GetMapping("/admin/coupons")
    public ResponseEntity<CouponResponse> getAllCoupons(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "code", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        CouponResponse couponResponse = couponService.getAllCoupons(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(couponResponse, HttpStatus.OK);
    }

    @GetMapping("/public/coupons/{code}")
    public ResponseEntity<CouponDTO> getCouponByCode(@PathVariable String code) {
        CouponDTO coupon = couponService.getCouponByCode(code);
        return new ResponseEntity<>(coupon, HttpStatus.OK);
    }

    @PostMapping("/public/users/{email}/carts/{cartId}/coupons/{code}/apply")
    public ResponseEntity<String> applyCoupon(@PathVariable String email, @PathVariable Long cartId, @PathVariable String code) {
        couponService.applyCoupon(email, cartId, code);
        return new ResponseEntity<>("Coupon applied successfully!", HttpStatus.OK);
    }
    
}
