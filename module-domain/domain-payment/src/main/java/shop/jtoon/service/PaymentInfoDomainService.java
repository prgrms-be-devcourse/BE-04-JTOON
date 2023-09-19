package shop.jtoon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.jtoon.dto.PaymentDto;
import shop.jtoon.dto.PaymentInfoDto;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.PaymentInfo;
import shop.jtoon.exception.DuplicatedException;
import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.repository.PaymentInfoRepository;
import shop.jtoon.repository.PaymentInfoSearchRepository;
import shop.jtoon.type.ErrorStatus;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentInfoDomainService {

    private final PaymentInfoRepository paymentInfoRepository;
    private final PaymentInfoSearchRepository paymentInfoSearchRepository;

    @Transactional
    public void createPaymentInfo(PaymentDto paymentDto) {
        Member member = null; // TODO: member 조회 기능 추가
        PaymentInfo paymentInfo = paymentDto.toEntity(member);
        paymentInfoRepository.save(paymentInfo);
    }

    public void validatePaymentInfo(PaymentDto paymentDto) {
        BigDecimal cookieAmount = paymentDto.cookieItem().getAmount();
        validateAmount(paymentDto.amount(), cookieAmount);
        validateImpUid(paymentDto.impUid());
        validateMerchantUid(paymentDto.merchantUid());
    }

    public List<PaymentInfoDto> getPaymentsInfo(List<String> merchantsUid) {
        Member member = null; // TODO: member 조회 기능 추가
        List<PaymentInfo> paymentsInfo = paymentInfoSearchRepository.searchByMerchantsUidAndEmail(
                merchantsUid,
                member.getEmail()
        );

        return paymentsInfo.stream()
                .map(PaymentInfoDto::toDto)
                .toList();
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
