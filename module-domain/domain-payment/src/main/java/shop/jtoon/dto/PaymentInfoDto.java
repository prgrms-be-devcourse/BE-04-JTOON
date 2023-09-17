package shop.jtoon.dto;

import lombok.Builder;
import shop.jtoon.entity.PaymentInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PaymentInfoDto(
        String itemName,
        int itemCount,
        BigDecimal amount,
        LocalDateTime createdAt
) {

    public static PaymentInfoDto toDto(PaymentInfo paymentInfo) {
        return PaymentInfoDto.builder()
                .itemName(paymentInfo.getCookieItem().getItemName())
                .itemCount(paymentInfo.getCookieItem().getCount())
                .amount(paymentInfo.getAmount())
                .createdAt(paymentInfo.getCreatedAt())
                .build();
    }
}
