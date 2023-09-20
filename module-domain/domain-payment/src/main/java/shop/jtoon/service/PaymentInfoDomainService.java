package shop.jtoon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.jtoon.entity.CookieItem;
import shop.jtoon.exception.DuplicatedException;
import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.repository.PaymentInfoRepository;
import shop.jtoon.type.ErrorStatus;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentInfoDomainService {

    private final PaymentInfoRepository paymentInfoRepository;

    public void validatePaymentInfo(String impUid, String merchantUid, String itemName, BigDecimal amount) {
        CookieItem cookieItem = CookieItem.from(itemName);
        validateAmount(amount, cookieItem.getAmount());
        validateImpUid(impUid);
        validateMerchantUid(merchantUid);
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
