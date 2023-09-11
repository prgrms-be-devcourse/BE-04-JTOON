package com.devtoon.jtoon.payment.application;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.paymentinfo.request.CancelReq;
import com.devtoon.jtoon.paymentinfo.request.PaymentReq;
import com.devtoon.jtoon.paymentinfo.service.PaymentInfoService;
import com.devtoon.jtoon.security.domain.jwt.MemberThreadLocal;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentInfoService paymentInfoService;

	public BigDecimal validatePayment(PaymentReq paymentReq)
		throws IamportResponseException, IOException {
		Member member = MemberThreadLocal.getMember();
		paymentInfoService.validateIamport(paymentReq);

		return paymentInfoService.createPayment(paymentReq, member);
	}

	public IamportResponse<Payment> cancelPayment(CancelReq cancelReq)
		throws IamportResponseException, IOException {
		return paymentInfoService.cancelPayment(cancelReq);
	}
}
