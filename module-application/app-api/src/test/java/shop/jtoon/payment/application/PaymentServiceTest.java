package shop.jtoon.payment.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.jtoon.dto.MemberDto;
import shop.jtoon.entity.Member;
import shop.jtoon.member.application.MemberService;
import shop.jtoon.payment.factory.PaymentFactory;
import shop.jtoon.payment.request.CancelReq;
import shop.jtoon.payment.request.ConditionReq;
import shop.jtoon.payment.request.PaymentReq;
import shop.jtoon.payment.response.PaymentRes;
import shop.jtoon.service.IamportService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private IamportService iamportService;

    @Mock
    private PaymentInfoService paymentInfoService;

    @Mock
    private MemberCookieService memberCookieService;

    @Mock
    private MemberService memberService;

    private PaymentReq paymentReq;
    private CancelReq cancelReq;
    private Member member;
    private MemberDto memberDto;

    @BeforeEach
    void beforeEach() {
        member = PaymentFactory.createMember("exam123@naver.com");
        memberDto = PaymentFactory.createMemberDto(1L, member);
        paymentReq = PaymentFactory.createPaymentReq("imp123", "mer123", member.getEmail());
        cancelReq = PaymentFactory.createCancelReq(paymentReq);
    }

    @DisplayName("validateAndCreatePayment - 결제에 대한 검증 및 생성 서비스를 정상적으로 호출하는 지 검증, - BigDecimal(Amount)")
    @Test
    void validateAndCreatePayment_Amount() {
        // Given
        given(memberService.findById(any(Long.class))).willReturn(member);

        // When
        BigDecimal actual = paymentService.validateAndCreatePayment(paymentReq, memberDto);

        // Then
        verify(iamportService).validateIamport(any(String.class), any(BigDecimal.class));
        verify(paymentInfoService).validatePaymentInfo(any(PaymentReq.class));
        verify(paymentInfoService).createPaymentInfo(any(PaymentReq.class), any(Member.class));
        verify(memberCookieService).createMemberCookie(any(String.class), any(Member.class));
        assertThat(actual).isEqualTo(paymentReq.amount());
    }

    @DisplayName("cancelPayment - 결제 취소에 대한 검증 및 취소 서비스를 정상적으로 호출하는 지 검증, - Void ")
    @Test
    void cancelPayment_Void() {
        // When
        paymentService.cancelPayment(cancelReq);

        // Then
        verify(iamportService).validateIamport(any(String.class), any(BigDecimal.class));
        verify(iamportService).cancelIamport(any(String.class), any(String.class), any(BigDecimal.class),
                any(String.class));
    }

    @DisplayName("getPayments - 결제 내역 조회에 대한 서비스를 정상적으로 호출하는 지 검증 - PaymentRes List")
    @Test
    void getPayments_PaymentRes_List() {
        // Given
        ConditionReq conditionReq = ConditionReq.builder()
                .merchantsUid(Collections.emptyList())
                .build();
        given(memberService.findById(any(Long.class))).willReturn(member);
        given(paymentInfoService.getPaymentsInfo(anyList(), any(Member.class))).willReturn(Collections.emptyList());

        // When
        List<PaymentRes> actual = paymentService.getPayments(conditionReq, memberDto);

        // Then
        assertThat(actual).isEmpty();
    }
}
