package shop.jtoon.payment.factory;

import shop.jtoon.dto.MemberDto;
import shop.jtoon.entity.*;
import shop.jtoon.payment.request.CancelReq;
import shop.jtoon.payment.request.PaymentReq;

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

    public static PaymentReq createPaymentReq(String impUid, String merchantUid, String email) {
        return PaymentReq.builder()
                .impUid(impUid)
                .merchantUid(merchantUid)
                .payMethod("card")
                .itemName(CookieItem.COOKIE_ONE.getItemName())
                .amount(CookieItem.COOKIE_ONE.getAmount())
                .buyerEmail(email)
                .buyerName("홍도산")
                .buyerPhone("01012311231")
                .build();
    }

    public static CancelReq createCancelReq(PaymentReq paymentReq) {
        return CancelReq.builder()
                .impUid(paymentReq.impUid())
                .merchantUid(paymentReq.merchantUid())
                .reason("reason")
                .checksum(CookieItem.COOKIE_ONE.getAmount())
                .refundHolder(paymentReq.buyerEmail())
                .build();
    }

    public static Member createMember(String email) {
        return Member.builder()
                .email(email)
                .password("Qwe123!!")
                .name("홍도산")
                .nickname("개발을담다")
                .gender(Gender.MALE)
                .phone("01012331233")
                .role(Role.USER)
                .loginType(LoginType.LOCAL)
                .build();
    }

    public static MemberDto createMemberDto(Long id, Member member) {
        return MemberDto.builder()
                .id(id)
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .gender(member.getGender())
                .phone(member.getPhone())
                .role(member.getRole())
                .build();
    }
}
