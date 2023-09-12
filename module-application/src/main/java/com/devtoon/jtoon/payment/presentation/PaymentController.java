package com.devtoon.jtoon.payment.presentation;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devtoon.jtoon.payment.application.PaymentApplicationService;
import com.devtoon.jtoon.payment.request.CancelReq;
import com.devtoon.jtoon.payment.request.PaymentReq;
import com.devtoon.jtoon.payment.service.IamportService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

	private final PaymentApplicationService paymentApplicationService;
	private final IamportService iamportService;

	@PostMapping("/validation")
	public BigDecimal validatePayment(@RequestBody @Valid PaymentReq paymentReq)
		throws IamportResponseException, IOException {
		return paymentApplicationService.validatePayment(paymentReq);
	}

	@PostMapping("/cancel")
	public IamportResponse<Payment> cancelPayment(@RequestBody @Valid CancelReq cancelReq)
		throws IamportResponseException, IOException {
		return iamportService.cancelPayment(cancelReq);
	}
}
