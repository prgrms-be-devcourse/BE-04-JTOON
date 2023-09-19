package shop.jtoon.payment.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shop.jtoon.annotation.CurrentUser;
import shop.jtoon.dto.MemberDto;
import shop.jtoon.dto.PaymentInfoRes;
import shop.jtoon.payment.application.PaymentApplicationService;
import shop.jtoon.payment.request.CancelReq;
import shop.jtoon.payment.request.ConditionReq;
import shop.jtoon.payment.request.PaymentReq;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentApplicationService paymentApplicationService;

    @PostMapping("/validation")
    @ResponseStatus(HttpStatus.CREATED)
    public BigDecimal validatePayment(@RequestBody @Valid PaymentReq paymentReq, @CurrentUser MemberDto memberDto) {
        return paymentApplicationService.validatePayment(paymentReq, memberDto);
    }

    @PostMapping("/cancel")
    @ResponseStatus(HttpStatus.OK)
    public void cancelPayment(@RequestBody @Valid CancelReq cancelReq) {
        paymentApplicationService.cancelPayment(cancelReq);
    }

    @PostMapping("/search")
    public List<PaymentInfoRes> getPayments(@RequestBody ConditionReq conditionReq, @CurrentUser MemberDto memberDto) {
        return paymentApplicationService.getPayments(conditionReq, memberDto);
    }
}
