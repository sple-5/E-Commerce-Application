package com.app.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaymentDetailsDTO {
    @NotBlank(message = "Card number is required")
    @Pattern(regexp = "\\d{16}", message = "Invalid card number format")
    private String cardNumber;
    
    @NotBlank(message = "CVC is required")
    @Pattern(regexp = "\\d{3,4}", message = "Invalid CVC format")
    private String cvc;
    
    @NotBlank(message = "Expiration date is required")
    @Pattern(regexp = "^(0[1-9]|1[0-2])\\/\\d{2}$", message = "Invalid expiration date format (MM/YY)")
    private String expirationDate;
}