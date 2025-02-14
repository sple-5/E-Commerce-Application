package com.app.services;

import com.app.entites.Cart;
import com.app.entites.Coupon;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.CouponDTO;
import com.app.payloads.CouponResponse;
import com.app.repositories.CartRepo;
import com.app.repositories.CouponRepository;
import com.app.repositories.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private CartRepo cartRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CouponDTO createCoupon(CouponDTO couponDTO) {
        
        if (couponRepository.existsByCode(couponDTO.getCode())) {
            throw new IllegalArgumentException("Coupon with code '" + couponDTO.getCode() + "' already exists.");
        }
    
        Coupon coupon = modelMapper.map(couponDTO, Coupon.class);
        Coupon savedCoupon = couponRepository.save(coupon);
    
        return modelMapper.map(savedCoupon, CouponDTO.class);
    }
    

    @Override
    public CouponResponse getAllCoupons(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        return null;
    }

    @Override
    public CouponDTO getCouponByCode(String code) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "coupon_name", code));
        return modelMapper.map(coupon, CouponDTO.class);
    }

    @Transactional
    @Override
    public void applyCoupon(String email, Long cartId, String code) {
        userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));
    
        if (!cart.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("Cart does not belong to the user!");
        }
    
        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cannot apply a coupon to an empty cart.");
        }
    
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "code", code));
    
        if (!coupon.isValid()) {
            throw new IllegalStateException("Coupon is expired or usage limit reached.");
        }
    
        if (cart.getAppliedCoupon() != null) {
            if (cart.getAppliedCoupon().getCode().equals(code)) {
                return;
            } else {
                cart.getAppliedCoupon().setUsedCount(cart.getAppliedCoupon().getUsedCount() - 1); 
            }
        }
    
        coupon.useCoupon();
        cart.setAppliedCoupon(coupon);
        cart.recalculateTotal();
    
        couponRepository.save(coupon);
        cartRepository.save(cart);
    }
    

}
