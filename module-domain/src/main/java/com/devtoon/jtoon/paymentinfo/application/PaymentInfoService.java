package com.devtoon.jtoon.paymentinfo.application;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.member.repository.MemberRepository;
import com.devtoon.jtoon.paymentinfo.entity.CookieItem;
import com.devtoon.jtoon.paymentinfo.entity.PaymentInfo;
import com.devtoon.jtoon.paymentinfo.repository.PaymentInfoRepository;
import com.devtoon.jtoon.paymentinfo.request.CancelReq;
import com.devtoon.jtoon.paymentinfo.request.PaymentReq;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

@Service
@Transactional(readOnly = true)
public class PaymentInfoService {

	@Value("${pg.kg-inicis.rest-api-key}")
	private String REST_API_KEY;

	@Value("${pg.kg-inicis.rest-api-secret}")
	private String REST_API_SECRET;

	private final IamportClient iamportClient;
	private final MemberRepository memberRepository;
	private final PaymentInfoRepository paymentInfoRepository;

	public PaymentInfoService(MemberRepository memberRepository, PaymentInfoRepository paymentInfoRepository) {
		this.memberRepository = memberRepository;
		this.paymentInfoRepository = paymentInfoRepository;
		this.iamportClient = new IamportClient(REST_API_KEY, REST_API_SECRET);
	}

	public IamportResponse<Payment> validateIamport(PaymentReq paymentReq)
		throws IamportResponseException, IOException {
		IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(paymentReq.impUid());
		validateAmount(iamportResponse, paymentReq.amount());
		CookieItem cookieItem = CookieItem.from(paymentReq.cookieItem());
		validateAmount(iamportResponse, cookieItem.getAmount());
		validateImpUid(paymentReq);
		validateMerchantUid(paymentReq);

		return iamportResponse;
	}

	@Transactional
	public int createPaymentInfo(PaymentReq paymentReq) {
		Member member = memberRepository.findByPhone(paymentReq.buyerPhone())
			.orElseThrow(() -> new RuntimeException("member is not found"));
		PaymentInfo paymentInfo = paymentReq.toEntity(member);

		return paymentInfoRepository.save(paymentInfo)
			.getAmount();
	}

	public IamportResponse<Payment> cancelPayments(CancelReq cancelReq) {
		return null;
	}

	private void validateAmount(IamportResponse<Payment> iamportResponse, int amount) {
		int realAmount = iamportResponse.getResponse()
			.getAmount()
			.intValue();

		if (realAmount != amount) {
			throw new RuntimeException("verify iamport exception");
		}
	}

	private void validateMerchantUid(PaymentReq paymentReq) {
		if (paymentInfoRepository.existsByMerchantUid(paymentReq.merchantUid())) {
			throw new RuntimeException("merchantUid duplicate");
		}
	}

	private void validateImpUid(PaymentReq paymentReq) {
		if (paymentInfoRepository.existsByImpUid(paymentReq.impUid())) {
			throw new RuntimeException("impUid duplicate");
		}
	}
}
