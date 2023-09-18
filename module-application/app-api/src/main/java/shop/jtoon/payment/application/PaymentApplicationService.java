package shop.jtoon.payment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.jtoon.dto.CancelDto;
import shop.jtoon.dto.PaymentDto;
import shop.jtoon.dto.PaymentInfoRes;
import shop.jtoon.payment.request.CancelReq;
import shop.jtoon.payment.request.ConditionReq;
import shop.jtoon.payment.request.PaymentReq;
import shop.jtoon.service.IamportService;
import shop.jtoon.service.MemberCookieDomainService;
import shop.jtoon.service.PaymentInfoDomainService;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentApplicationService {

    private final IamportService iamportService;
    private final PaymentInfoDomainService paymentInfoDomainService;
    private final MemberCookieDomainService memberCookieDomainService;

    @Transactional
    public BigDecimal validatePayment(PaymentReq paymentReq) {
        PaymentDto paymentDto = paymentReq.toDto();
        iamportService.validateIamport(paymentDto.impUid(), paymentDto.amount());
        paymentInfoDomainService.validatePaymentInfo(paymentDto);
        paymentInfoDomainService.createPaymentInfo(paymentDto);
        memberCookieDomainService.createMemberCookie(paymentDto.cookieItem());

        return paymentDto.amount();
    }

    @Transactional
    public void cancelPayment(CancelReq cancelReq) {
        CancelDto cancelDto = cancelReq.toDto();
        iamportService.validateIamport(cancelReq.impUid(), cancelDto.checksum());
        iamportService.cancelIamport(cancelDto);
    }

    public List<PaymentInfoRes> getPayments(ConditionReq conditionReq) {
        return paymentInfoDomainService.getPaymentsInfo(conditionReq.merchantsUid());
    }
}
