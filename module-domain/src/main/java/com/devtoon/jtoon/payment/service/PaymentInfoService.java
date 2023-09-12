package com.devtoon.jtoon.payment.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devtoon.jtoon.error.exception.DuplicatedException;
import com.devtoon.jtoon.error.exception.InvalidRequestException;
import com.devtoon.jtoon.error.model.ErrorStatus;
import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.member.entity.MemberCookie;
import com.devtoon.jtoon.payment.entity.CookieItem;
import com.devtoon.jtoon.payment.entity.PaymentInfo;
import com.devtoon.jtoon.payment.repository.MemberCookieRepository;
import com.devtoon.jtoon.payment.repository.PaymentInfoRepository;
import com.devtoon.jtoon.payment.request.PaymentReq;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentInfoService {

	private final PaymentInfoRepository paymentInfoRepository;
	private final MemberCookieRepository memberCookieRepository;

	@Transactional
	public BigDecimal createPayment(PaymentReq paymentReq, Member member) {
		PaymentInfo paymentInfo = paymentReq.toEntity(member);
		CookieItem cookieItem = CookieItem.from(paymentReq.cookieItem());
		MemberCookie memberCookie = MemberCookie.create(cookieItem.getCount(), member);
		paymentInfoRepository.save(paymentInfo);
		memberCookieRepository.save(memberCookie);

		return paymentInfo.getAmount();
	}

	public void validateAmount(IamportResponse<Payment> iamportResponse, BigDecimal amount) {
		BigDecimal realAmount = iamportResponse.getResponse().getAmount();

		if (!realAmount.equals(amount)) {
			throw new InvalidRequestException(ErrorStatus.PAYMENT_AMOUNT_INVALID);
		}
	}

	public void validateMerchantUid(PaymentReq paymentReq) {
		if (paymentInfoRepository.existsByMerchantUid(paymentReq.merchantUid())) {
			throw new DuplicatedException(ErrorStatus.PAYMENT_MERCHANT_UID_DUPLICATED);
		}
	}

	public void validateImpUid(PaymentReq paymentReq) {
		if (paymentInfoRepository.existsByImpUid(paymentReq.impUid())) {
			throw new DuplicatedException(ErrorStatus.PAYMENT_IMP_UID_DUPLICATED);
		}
	}
}
