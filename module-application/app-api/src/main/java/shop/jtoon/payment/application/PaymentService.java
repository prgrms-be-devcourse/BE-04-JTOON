package shop.jtoon.payment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.jtoon.dto.MemberDto;
import shop.jtoon.entity.Member;
import shop.jtoon.payment.request.CancelReq;
import shop.jtoon.payment.request.ConditionReq;
import shop.jtoon.payment.request.PaymentReq;
import shop.jtoon.payment.response.PaymentRes;
import shop.jtoon.service.IamportService;
import shop.jtoon.service.MemberDomainService;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final IamportService iamportService;
    private final PaymentInfoService paymentInfoService;
    private final MemberCookieService memberCookieService;
    private final MemberDomainService memberDomainService;

    @Transactional
    public BigDecimal validateAndCreatePayment(PaymentReq paymentReq, MemberDto memberDto) {
        Member member = memberDomainService.findByEmail(memberDto.email());
        iamportService.validateIamport(paymentReq.impUid(), paymentReq.amount());
        paymentInfoService.validatePaymentInfo(paymentReq);
        paymentInfoService.createPaymentInfo(paymentReq, member);
        memberCookieService.createMemberCookie(paymentReq.itemName(), member);

        return paymentReq.amount();
    }

    @Transactional
    public void cancelPayment(CancelReq cancelReq) {
        iamportService.validateIamport(cancelReq.impUid(), cancelReq.checksum());
        iamportService.cancelIamport(
                cancelReq.impUid(),
                cancelReq.reason(),
                cancelReq.checksum(),
                cancelReq.refundHolder()
        );
    }

    public List<PaymentRes> getPayments(ConditionReq conditionReq, MemberDto memberDto) {
        Member member = memberDomainService.findByEmail(memberDto.email());

        return paymentInfoService.getPaymentsInfo(conditionReq.merchantsUid(), member);
    }
}
