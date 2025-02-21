package com.app.services;

import com.app.entites.AmountCoupon;
import com.app.entites.Coupon;
import com.app.entites.PercentageCoupon;
import com.app.payloads.CouponDTO;
import com.app.payloads.CouponResponse;
import com.app.repositories.CouponRepository;
import com.app.repositories.CartRepo;
import com.app.entites.Cart;
import com.app.exceptions.APIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CartRepo cartRepo;

    @Override
    public CouponDTO createCoupon(CouponDTO couponDTO) {
        Coupon newCoupon;

        if ("AMOUNT".equalsIgnoreCase(couponDTO.getDiscountType())) {
            newCoupon = new AmountCoupon();
            ((AmountCoupon) newCoupon).setDiscountAmount(couponDTO.getDiscountAmount());
        } else if ("PERCENTAGE".equalsIgnoreCase(couponDTO.getDiscountType())) {
            newCoupon = new PercentageCoupon();
            ((PercentageCoupon) newCoupon).setDiscountPercentage(couponDTO.getDiscountPercentage());
        } else {
            throw new IllegalArgumentException("Invalid discount type. Allowed values: AMOUNT or PERCENTAGE.");
        }

        newCoupon.setCode(couponDTO.getCode());
        newCoupon.setUsageLimit(couponDTO.getUsageLimit());
        newCoupon.setExpirationDate(couponDTO.getExpirationDate());

        Coupon savedCoupon = couponRepository.save(newCoupon);

        return new CouponDTO(savedCoupon);
    }

    @Override
    public CouponResponse getAllCoupons(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Coupon> coupons = couponRepository.findAll(pageable);
        List<CouponDTO> couponDTOs = coupons.getContent()
                .stream()
                .map(CouponDTO::new)
                .collect(Collectors.toList());

        return new CouponResponse(couponDTOs, coupons.getNumber(), coupons.getSize(),
                coupons.getTotalElements(), coupons.getTotalPages(), coupons.isLast());
    }

    @Override
    public CouponDTO getCouponByCode(String code) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new APIException("Coupon not found"));
        return new CouponDTO(coupon);
    }

    @Override
    @Transactional
    public void applyCoupon(String email, Long cartId, String code) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new APIException("Cart not found"));

        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new APIException("Coupon not found"));

        if (!coupon.isValid()) {
            throw new APIException("Coupon is expired or has been used the maximum number of times.");
        }

        cart.applyCoupon(coupon);
        cartRepo.save(cart);
    }

    @Override
    @Transactional
    public void cancelCoupon(String email, Long cartId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new APIException("Cart not found"));

        if (cart.getAppliedCoupon() == null) {
            throw new APIException("No coupon applied to cancel.");
        }

        cart.setAppliedCoupon(null);
        cart.recalculateTotal();
        cartRepo.save(cart);
    }
}
