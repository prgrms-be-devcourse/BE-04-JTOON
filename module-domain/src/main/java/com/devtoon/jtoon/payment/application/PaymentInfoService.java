package com.devtoon.jtoon.payment.application;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.member.repository.MemberRepository;
import com.devtoon.jtoon.payment.entity.PaymentInfo;
import com.devtoon.jtoon.payment.repository.PaymentInfoRepository;
import com.devtoon.jtoon.payment.request.PaymentInfoDto;
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
		this.iamportClient = new IamportClient(REST_API_KEY, REST_API_SECRET);
		this.paymentInfoRepository = paymentInfoRepository;
		this.memberRepository = memberRepository;
	}

	public IamportResponse<Payment> paymentLookup(String impUid) throws IamportResponseException, IOException {
		return iamportClient.paymentByImpUid(impUid);
	}

	@Transactional
	public IamportResponse<Payment> verifyIamport(
		IamportResponse<Payment> iamportResponse,
		PaymentInfoDto paymentInfoDto
	) {
		validateImpUid(paymentInfoDto);
		validateMerchantUid(paymentInfoDto);
		validateAmount(iamportResponse, paymentInfoDto.amount());
		Member member = memberRepository.findByPhone(paymentInfoDto.buyerPhone())
			.orElseThrow(() -> new RuntimeException("member is not found"));
		PaymentInfo paymentInfo = paymentInfoDto.toEntity(member);
		paymentInfoRepository.save(paymentInfo);

		return iamportResponse;
	}

	private void validateAmount(IamportResponse<Payment> iamportResponse, int amount) {
		if (iamportResponse.getResponse().getAmount().intValue() != amount) {
			throw new RuntimeException("verify iamport exception");
		}
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
