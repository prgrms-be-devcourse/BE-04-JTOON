package shop.jtoon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.jtoon.entity.CookieItem;
import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.type.ErrorStatus;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentInfoDomainService {

    public void validatePaymentInfo(String itemName, BigDecimal amount) {
        CookieItem cookieItem = CookieItem.from(itemName);
        validateAmount(amount, cookieItem.getAmount());
    }

    private void validateAmount(BigDecimal amount, BigDecimal cookieAmount) {
        if (!amount.equals(cookieAmount)) {
            throw new InvalidRequestException(ErrorStatus.PAYMENT_AMOUNT_INVALID);
        }
    }
}
