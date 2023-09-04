package com.devtoon.jtoon.payment.presentation;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devtoon.jtoon.iamport.application.IamportService;
import com.devtoon.jtoon.payment.application.PaymentInfoService;
import com.devtoon.jtoon.payment.request.PaymentInfoDto;
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
	public IamportResponse<Payment> validateIamport(@RequestBody PaymentInfoDto paymentInfoDto)
		throws IamportResponseException, IOException {
		return iamportService.validateIamport(paymentInfoDto.impUid(), paymentInfoDto.amount());
	}

	@PostMapping
	public void createPaymentInfo(@RequestBody PaymentInfoDto paymentInfoDto) {
		paymentInfoService.createPaymentInfo(paymentInfoDto);
	}
}
