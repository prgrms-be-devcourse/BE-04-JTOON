package com.devtoon.jtoon.payment.presentation;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devtoon.jtoon.paymentinfo.application.PaymentInfoService;
import com.devtoon.jtoon.paymentinfo.request.CancelReq;
import com.devtoon.jtoon.paymentinfo.request.PaymentReq;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

	private final PaymentInfoService paymentInfoService;

	@PostMapping("/validation")
	public IamportResponse<Payment> validateIamport(@RequestBody PaymentReq paymentReq)
		throws IamportResponseException, IOException {
		return paymentInfoService.validateIamport(paymentReq);
	}

	@PostMapping
	public int createPaymentInfo(@RequestBody PaymentReq paymentReq) {
		return paymentInfoService.createPaymentInfo(paymentReq);
	}

	@PostMapping("/cancel")
	private IamportResponse<Payment> cancelPayments(@RequestBody CancelReq cancelReq) {
		return paymentInfoService.cancelPayments(cancelReq);
	}
}
