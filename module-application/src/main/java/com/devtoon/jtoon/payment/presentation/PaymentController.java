package com.devtoon.jtoon.payment.presentation;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devtoon.jtoon.iamport.application.IamportService;
import com.devtoon.jtoon.iamport.request.IamportReq;
import com.devtoon.jtoon.paymentinfo.application.PaymentInfoService;
import com.devtoon.jtoon.paymentinfo.entity.CookieItem;
import com.devtoon.jtoon.paymentinfo.request.PaymentInfoReq;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

	private final IamportService iamportService;
	private final PaymentInfoService paymentInfoService;

	@PostMapping("/validation")
	public IamportResponse<Payment> validateIamport(@RequestBody IamportReq iamportReq)
		throws IamportResponseException, IOException {
		int cookieAmount = CookieItem.from(iamportReq.cookieItem()).getAmount();

		return iamportService.validateIamport(iamportReq, cookieAmount);
	}

	@PostMapping
	public void createPaymentInfo(@RequestBody PaymentInfoReq paymentInfoReq) {
		paymentInfoService.createPaymentInfo(paymentInfoReq);
	}
}
