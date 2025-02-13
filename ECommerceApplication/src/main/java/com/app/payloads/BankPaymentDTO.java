package com.app.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankPaymentDTO {
	private Long paymentId;
	private String paymentMethod;
	private String bankName;
	@Getter @Setter private String accountNumber;
}
