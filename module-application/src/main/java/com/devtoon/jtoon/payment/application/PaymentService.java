package com.devtoon.jtoon.payment.application;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.payment.request.PaymentReq;
import com.devtoon.jtoon.payment.service.IamportService;
import com.devtoon.jtoon.payment.service.PaymentInfoService;
import com.devtoon.jtoon.security.domain.jwt.MemberThreadLocal;
import com.siot.IamportRestClient.exception.IamportResponseException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentInfoService paymentInfoService;
	private final IamportService iamportService;

	public BigDecimal validatePayment(PaymentReq paymentReq) throws IamportResponseException, IOException {
		Member member = MemberThreadLocal.getMember();
		iamportService.validateIamport(paymentReq);

		return paymentInfoService.createPayment(paymentReq, member);
	}
}
