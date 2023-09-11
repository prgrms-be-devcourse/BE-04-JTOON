package com.devtoon.jtoon.payment.presentation;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devtoon.jtoon.payment.application.PaymentService;
import com.devtoon.jtoon.paymentinfo.request.CancelReq;
import com.devtoon.jtoon.paymentinfo.request.PaymentReq;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/validation")
	public BigDecimal validatePayment(@RequestBody @Valid PaymentReq paymentReq)
		throws IamportResponseException, IOException {
		return paymentService.validatePayment(paymentReq);
	}

	@PostMapping("/cancel")
	public IamportResponse<Payment> cancelPayment(@RequestBody @Valid CancelReq cancelReq)
		throws IamportResponseException, IOException {
		return paymentService.cancelPayment(cancelReq);
	}
}
