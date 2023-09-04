package com.devtoon.jtoon.paymentinfo.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.member.repository.MemberRepository;
import com.devtoon.jtoon.paymentinfo.entity.PaymentInfo;
import com.devtoon.jtoon.paymentinfo.repository.PaymentInfoRepository;
import com.devtoon.jtoon.paymentinfo.request.PaymentInfoReq;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentInfoService {

	private final MemberRepository memberRepository;
	private final PaymentInfoRepository paymentInfoRepository;

	@Transactional
	public void createPaymentInfo(PaymentInfoReq paymentInfoReq) {
		validateImpUid(paymentInfoReq);
		validateMerchantUid(paymentInfoReq);
		Member member = memberRepository.findByPhone(paymentInfoReq.buyerPhone())
			.orElseThrow(() -> new RuntimeException("member is not found"));
		PaymentInfo paymentInfo = paymentInfoReq.toEntity(member);
		paymentInfoRepository.save(paymentInfo);
	}

	private void validateMerchantUid(PaymentInfoReq paymentInfoReq) {
		if (paymentInfoRepository.existsByMerchantUid(paymentInfoReq.merchantUid())) {
			throw new RuntimeException("merchantUid duplicate");
		}
	}

	private void validateImpUid(PaymentInfoReq paymentInfoReq) {
		if (paymentInfoRepository.existsByImpUid(paymentInfoReq.impUid())) {
			throw new RuntimeException("impUid duplicate");
		}
	}
}
