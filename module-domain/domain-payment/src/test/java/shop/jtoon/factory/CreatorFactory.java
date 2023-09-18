package shop.jtoon.factory;

import shop.jtoon.dto.PaymentDto;
import shop.jtoon.entity.*;

import java.math.BigDecimal;

public class CreatorFactory {

    public static PaymentInfo createPaymentInfo(String impUid, String merchantUid, Member member) {
        return PaymentInfo.builder()
            .impUid(impUid)
            .merchantUid(merchantUid)
            .payMethod("card")
            .cookieItem(CookieItem.COOKIE_ONE)
            .amount(BigDecimal.valueOf(1000))
            .member(member)
            .build();
    }

    public static PaymentDto createPaymentDto(String impUid, String merchantUid, String email) {
        return PaymentDto.builder()
            .impUid(impUid)
            .merchantUid(merchantUid)
            .payMethod("card")
            .cookieItem(CookieItem.COOKIE_ONE)
            .amount(BigDecimal.valueOf(1000))
            .buyerEmail(email)
            .build();
    }

    public static Member createMember(String email) {
        return Member.builder()
            .email(email)
            .password("qwe123!!")
            .name("홍도산")
            .nickname("개발을담다")
            .gender(Gender.MALE)
            .phone("01012331233")
            .role(Role.USER)
            .loginType(LoginType.LOCAL)
            .build();
    }
}
