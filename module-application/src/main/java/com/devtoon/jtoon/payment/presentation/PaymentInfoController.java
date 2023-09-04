package com.devtoon.jtoon.payment.presentation;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devtoon.jtoon.payment.application.PaymentInfoService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentInfoController {
	private final PaymentInfoService paymentInfoService;

	public IamportResponse<Payment> paymentLookup(String impUid) throws IamportResponseException, IOException {
		return paymentInfoService.paymentLookup(impUid);
	}
}
