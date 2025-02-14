package com.app.services;

import com.app.entites.Cart;
import com.app.entites.Coupon;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.CouponDTO;
import com.app.payloads.CouponResponse;
import com.app.repositories.CartRepo;
import com.app.repositories.CouponRepository;
import com.app.repositories.UserRepo;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();     
        
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Coupon> pageCoupons = couponRepository.findAll(pageDetails);
        
        List<Coupon> allCoupons = pageCoupons.getContent(); 

		if (allCoupons.size() == 0) {
			throw new APIException("Coupon is not yet available");
		}

		List<CouponDTO> couponDTOs = allCoupons.stream().map(p -> modelMapper.map(p, CouponDTO.class))
				.collect(Collectors.toList());

        CouponResponse couponResponse = new CouponResponse();

		couponResponse.setCoupons(couponDTOs);
		couponResponse.setPageNumber(pageCoupons.getNumber());
		couponResponse.setPageSize(pageCoupons.getSize());
		couponResponse.setTotalElements(pageCoupons.getTotalElements());
		couponResponse.setTotalPages(pageCoupons.getTotalPages());
		couponResponse.setLastPage(pageCoupons.isLast());

		return couponResponse;    
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
            throw new APIException("Cart does not belong to the user!");
        }
    
        if (cart.getCartItems().isEmpty()) {
            throw new APIException("Cannot apply a coupon to an empty cart.");
        }
    
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "code", code));
    
        if (!coupon.isValid()) {
            throw new APIException("Coupon is expired or usage limit reached.");
        }
    
        if (cart.getAppliedCoupon() != null) {
            if (cart.getAppliedCoupon().getCode().equals(code)) {
                return;
            } else {
                cart.getAppliedCoupon().setUsedCount(cart.getAppliedCoupon().getUsedCount() - 1); 
            }
        }
        
        cart.setAppliedCoupon(coupon);
        cart.recalculateTotal();
    
        couponRepository.save(coupon);
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void cancelCoupon(String email, Long cartId) {
        userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));

        if (!cart.getUser().getEmail().equals(email)) {
            throw new APIException("Cart does not belong to the user!");
        }

        if (cart.getAppliedCoupon() == null) {
            throw new APIException("Coupon has not been applied to the cart!");
        }

        Coupon coupon = cart.cancelCoupon();

        if (coupon.equals(null)) {
            throw new APIException("Coupon is missing from cart!");
        }
        
        cartRepository.save(cart);
        couponRepository.save(coupon);
    }
    

}
