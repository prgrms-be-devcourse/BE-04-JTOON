package shop.jtoon.payment.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import shop.jtoon.entity.CookieItem;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.PaymentInfo;

import java.math.BigDecimal;

import static shop.jtoon.util.RegExp.EMAIL_PATTERN;
import static shop.jtoon.util.RegExp.PHONE_PATTERN;

@Builder
public record PaymentReq(
        @NotBlank String impUid,
        @NotBlank String merchantUid,
        @NotBlank String payMethod,
        @NotBlank String itemName,
        @NotNull @DecimalMin("1") BigDecimal amount,
        @Pattern(regexp = EMAIL_PATTERN) String buyerEmail,
        @NotBlank @Size(max = 10) String buyerName,
        @Pattern(regexp = PHONE_PATTERN) String buyerPhone
) {

    public PaymentInfo toEntity(Member member) {
        return PaymentInfo.builder()
                .impUid(this.impUid)
                .merchantUid(this.merchantUid)
                .payMethod(this.payMethod)
                .cookieItem(CookieItem.from(this.itemName))
                .amount(this.amount)
                .member(member)
                .build();
    }
}
