package shop.jtoon.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.jtoon.dto.PaymentDto;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.PaymentInfo;
import shop.jtoon.exception.DuplicatedException;
import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.repository.PaymentInfoRepository;
import shop.jtoon.type.ErrorStatus;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentInfoDomainService {

	private final PaymentInfoRepository paymentInfoRepository;

	@Transactional
	public void createPayment(PaymentDto paymentDto) {
		Member member = null; // TODO: member 조회 기능 추가
		PaymentInfo paymentInfo = paymentDto.toEntity(member);
		paymentInfoRepository.save(paymentInfo);
	}

	public void validatePayment(PaymentDto paymentDto) {
		BigDecimal cookieAmount = paymentDto.cookieItem().getAmount();
		validateAmount(paymentDto.amount(), cookieAmount);
		validateImpUid(paymentDto.impUid());
		validateMerchantUid(paymentDto.merchantUid());
	}

	private void validateAmount(BigDecimal amount, BigDecimal cookieAmount) {
		if (!amount.equals(cookieAmount)) {
			throw new InvalidRequestException(ErrorStatus.PAYMENT_AMOUNT_INVALID);
		}
	}

	private void validateImpUid(String impUid) {
		if (paymentInfoRepository.existsByImpUid(impUid)) {
			throw new DuplicatedException(ErrorStatus.PAYMENT_IMP_UID_DUPLICATED);
		}
	}

	private void validateMerchantUid(String merchantUid) {
		if (paymentInfoRepository.existsByMerchantUid(merchantUid)) {
			throw new DuplicatedException(ErrorStatus.PAYMENT_MERCHANT_UID_DUPLICATED);
		}
	}
}
