package shop.jtoon.dto;

import lombok.Builder;
import shop.jtoon.entity.PaymentInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PaymentInfoRes(
    String itemName,
    int itemCount,
    BigDecimal amount,
    LocalDateTime createdAt
) {

    public static PaymentInfoRes toDto(PaymentInfo paymentInfo) {
        return PaymentInfoRes.builder()
            .itemName(paymentInfo.getCookieItem().getItemName())
            .itemCount(paymentInfo.getCookieItem().getCount())
            .amount(paymentInfo.getAmount())
            .createdAt(paymentInfo.getCreatedAt())
            .build();
    }
}
