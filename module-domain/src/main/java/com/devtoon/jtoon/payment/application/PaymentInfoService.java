package com.devtoon.jtoon.payment.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.member.repository.MemberRepository;
import com.devtoon.jtoon.payment.entity.PaymentInfo;
import com.devtoon.jtoon.payment.repository.PaymentInfoRepository;
import com.devtoon.jtoon.payment.request.PaymentInfoDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentInfoService {

	private final MemberRepository memberRepository;
	private final PaymentInfoRepository paymentInfoRepository;

	@Transactional
	public void createPaymentInfo(PaymentInfoDto paymentInfoDto) {
		validateImpUid(paymentInfoDto);
		validateMerchantUid(paymentInfoDto);
		Member member = memberRepository.findByPhone(paymentInfoDto.buyerPhone())
			.orElseThrow(() -> new RuntimeException("member is not found"));
		PaymentInfo paymentInfo = paymentInfoDto.toEntity(member);
		paymentInfoRepository.save(paymentInfo);
	}

	private void validateMerchantUid(PaymentInfoDto paymentInfoDto) {
		if (paymentInfoRepository.existsByMerchantUid(paymentInfoDto.merchantUid())) {
			throw new RuntimeException("merchantUid duplicate");
		}
	}

	private void validateImpUid(PaymentInfoDto paymentInfoDto) {
		if (paymentInfoRepository.existsByImpUid(paymentInfoDto.impUid())) {
			throw new RuntimeException("impUid duplicate");
		}
	}
}
