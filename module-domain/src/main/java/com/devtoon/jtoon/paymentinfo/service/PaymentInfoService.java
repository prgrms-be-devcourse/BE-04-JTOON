package com.devtoon.jtoon.paymentinfo.service;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devtoon.jtoon.error.exception.DuplicatedException;
import com.devtoon.jtoon.error.exception.InvalidRequestException;
import com.devtoon.jtoon.error.model.ErrorStatus;
import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.member.entity.MemberCookie;
import com.devtoon.jtoon.member.repository.MemberCookieRepository;
import com.devtoon.jtoon.paymentinfo.entity.CookieItem;
import com.devtoon.jtoon.paymentinfo.entity.PaymentInfo;
import com.devtoon.jtoon.paymentinfo.repository.PaymentInfoRepository;
import com.devtoon.jtoon.paymentinfo.request.CancelReq;
import com.devtoon.jtoon.paymentinfo.request.PaymentReq;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

@Service
@Transactional(readOnly = true)
public class PaymentInfoService {

	private final IamportClient iamportClient;
	private final PaymentInfoRepository paymentInfoRepository;
	private final MemberCookieRepository memberCookieRepository;

	public PaymentInfoService(
		@Value("${pg.kg-inicis.rest-api-key}") String restApiKey,
		@Value("${pg.kg-inicis.rest-api-secret}") String restSecretKey,
		PaymentInfoRepository paymentInfoRepository,
		MemberCookieRepository memberCookieRepository
	) {
		this.iamportClient = new IamportClient(restApiKey, restSecretKey);
		this.paymentInfoRepository = paymentInfoRepository;
		this.memberCookieRepository = memberCookieRepository;
	}

	@Transactional
	public BigDecimal createPayment(PaymentReq paymentReq, Member member) {
		PaymentInfo paymentInfo = paymentReq.toEntity(member);
		CookieItem cookieItem = CookieItem.from(paymentReq.cookieItem());
		MemberCookie memberCookie = MemberCookie.create(cookieItem.getCount(), member);
		paymentInfoRepository.save(paymentInfo);
		memberCookieRepository.save(memberCookie);

		return paymentInfo.getAmount();
	}

	public IamportResponse<Payment> cancelPayment(CancelReq cancelReq)
		throws IamportResponseException, IOException {
		IamportResponse<Payment> irsp = iamportClient.paymentByImpUid(cancelReq.impUid());
		validateAmount(irsp, cancelReq.checksum());
		CancelData cancelData = cancelReq.toCancelData(irsp);

		return iamportClient.cancelPaymentByImpUid(cancelData);
	}

	public void validateIamport(PaymentReq paymentReq) throws IamportResponseException, IOException {
		IamportResponse<Payment> irsp = iamportClient.paymentByImpUid(paymentReq.impUid());
		CookieItem cookieItem = CookieItem.from(paymentReq.cookieItem());
		validateAmount(irsp, cookieItem.getAmount());
		validateAmount(irsp, paymentReq.amount());
		validateImpUid(paymentReq);
		validateMerchantUid(paymentReq);
	}

	private void validateAmount(IamportResponse<Payment> iamportResponse, BigDecimal amount) {
		BigDecimal realAmount = iamportResponse.getResponse().getAmount();

		if (!realAmount.equals(amount)) {
			throw new InvalidRequestException(ErrorStatus.PAYMENT_AMOUNT_INVALID);
		}
	}

	private void validateMerchantUid(PaymentReq paymentReq) {
		if (paymentInfoRepository.existsByMerchantUid(paymentReq.merchantUid())) {
			throw new DuplicatedException(ErrorStatus.PAYMENT_MERCHANT_UID_DUPLICATED);
		}
	}

	private void validateImpUid(PaymentReq paymentReq) {
		if (paymentInfoRepository.existsByImpUid(paymentReq.impUid())) {
			throw new DuplicatedException(ErrorStatus.PAYMENT_IMP_UID_DUPLICATED);
		}
	}
}
