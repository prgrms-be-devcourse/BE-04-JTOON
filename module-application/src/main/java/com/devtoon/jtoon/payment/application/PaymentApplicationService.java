package com.devtoon.jtoon.payment.application;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.devtoon.jtoon.payment.request.PaymentReq;
import com.devtoon.jtoon.payment.service.IamportService;
import com.devtoon.jtoon.payment.service.PaymentInfoDomainService;
import com.siot.IamportRestClient.exception.IamportResponseException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentApplicationService {

	private final IamportService iamportService;
	private final PaymentInfoDomainService paymentInfoDomainService;

	public BigDecimal validatePayment(PaymentReq paymentReq) throws IamportResponseException, IOException {
		iamportService.validateIamport(paymentReq);

		return paymentInfoDomainService.createPayment(paymentReq);
	}
}
