package shop.jtoon.factory;

import shop.jtoon.entity.CookieItem;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.PaymentInfo;
import shop.jtoon.payment.request.CancelReq;
import shop.jtoon.payment.request.PaymentReq;

import java.math.BigDecimal;

public class PaymentFactory {

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

    public static PaymentReq createPaymentReq(String impUid, String merchantUid, BigDecimal amount, String email) {
        return PaymentReq.builder()
                .impUid(impUid)
                .merchantUid(merchantUid)
                .payMethod("card")
                .itemName(CookieItem.COOKIE_ONE.getItemName())
                .amount(amount)
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
}
