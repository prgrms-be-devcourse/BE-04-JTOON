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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

	private final PaymentInfoService paymentInfoService;

	@PostMapping("/validation")
	public IamportResponse<Payment> validatePayment(@RequestBody @Valid PaymentReq paymentReq)
		throws IamportResponseException, IOException {
		IamportResponse<Payment> iamportResponse = paymentInfoService.paymentLookUp(paymentReq.impUid());
		paymentInfoService.validateIamport(paymentReq, iamportResponse);
		paymentInfoService.createPaymentInfo(paymentReq);

		return iamportResponse;
	}

	@PostMapping("/cancel")
	public IamportResponse<Payment> cancelPayment(@RequestBody @Valid CancelReq cancelReq)
		throws IamportResponseException, IOException {
		IamportResponse<Payment> iamportResponse = paymentInfoService.paymentLookUp(cancelReq.impUid());

		return paymentInfoService.cancelPayment(cancelReq, iamportResponse);
	}
}
