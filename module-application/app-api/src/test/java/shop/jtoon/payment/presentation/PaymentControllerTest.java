package shop.jtoon.payment.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.PaymentInfo;
import shop.jtoon.exception.IamportException;
import shop.jtoon.payment.factory.PaymentFactory;
import shop.jtoon.payment.factory.PaymentSnippetFactory;
import shop.jtoon.payment.request.PaymentReq;
import shop.jtoon.repository.MemberRepository;
import shop.jtoon.repository.PaymentInfoRepository;
import shop.jtoon.security.service.AuthenticationService;
import shop.jtoon.service.IamportService;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static shop.jtoon.type.ErrorStatus.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaymentInfoRepository paymentInfoRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @MockBean
    private IamportService iamportService;

    private String impUid;
    private String merchantUid;
    private Member member;


    @BeforeEach
    void beforeEach() {
        impUid = "impUid123";
        merchantUid = "merchant123";
        member = PaymentFactory.createMember("example123@naver.com");
        memberRepository.save(member);
        Authentication auth = authenticationService.getAuthentication(member.getEmail());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @DisplayName("POST: /payments/validation - 결제 승인 후 결제 정보에 대해 검증 및 생성이 성공적으로 됐을 때, - Amount")
    @Test
    void validatePayment_Amount() throws Exception {
        // Given
        PaymentReq paymentReq = PaymentFactory.createPaymentReq(impUid, merchantUid, member.getEmail());
        willDoNothing()
                .given(iamportService)
                .validateIamport(any(String.class), any(BigDecimal.class));

        // When, Then
        mockMvc.perform(post("/payments/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentReq)))
                .andDo(print())
                .andDo(document("payments/validation",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        PaymentSnippetFactory.PAYMENT_REQUEST))
                .andExpect(status().isCreated())
                .andExpect(content().string(paymentReq.amount().toString()));
    }

    @DisplayName("POST: /payments/validation - 결제 승인 정보가 조회되지 않거나 실제 결제 금액과 요청 금액이 다를 때, - IamportException")
    @Test
    void validatePayment_IamportException() throws Exception {
        // Given
        PaymentReq paymentReq = PaymentFactory.createPaymentReq(impUid, merchantUid, member.getEmail());
        willThrow(IamportException.class)
                .given(iamportService)
                .validateIamport(any(String.class), any(BigDecimal.class));

        // When, Then
        mockMvc.perform(post("/payments/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentReq)))
                .andDo(print())
                .andDo(document("payments/validation",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        PaymentSnippetFactory.PAYMENT_REQUEST))
                .andExpect(status().isInternalServerError());
    }

    @DisplayName("POST: /payments/validation - 결제 정보의 쿠키 가격과 실제 서버에서 알고 있는 쿠키 가격이 다를 때, - InvalidRequestException")
    @Test
    void validatePayment_InvalidRequestException() throws Exception {
        // Given
        PaymentReq paymentReq = PaymentFactory.createPaymentReq(impUid, merchantUid, BigDecimal.ONE, member.getEmail());
        willDoNothing()
                .given(iamportService)
                .validateIamport(any(String.class), any(BigDecimal.class));

        // When, Then
        mockMvc.perform(post("/payments/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentReq)))
                .andDo(print())
                .andDo(document("payments/validation",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        PaymentSnippetFactory.PAYMENT_REQUEST))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(PAYMENT_AMOUNT_INVALID.getMessage()));
    }

    @DisplayName("POST: /payments/validation - 포트원 결제 고유번호가 중복 됐을 때, - ImpUid DuplicatedException")
    @Test
    void validatePayment_ImpUid_DuplicatedException() throws Exception {
        // Given
        PaymentInfo paymentInfo = PaymentFactory.createPaymentInfo(impUid, merchantUid + "7", member);
        PaymentReq paymentReq = PaymentFactory.createPaymentReq(impUid, merchantUid, member.getEmail());
        paymentInfoRepository.save(paymentInfo);
        willDoNothing()
                .given(iamportService)
                .validateIamport(any(String.class), any(BigDecimal.class));

        // When, Then
        mockMvc.perform(post("/payments/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentReq)))
                .andDo(print())
                .andDo(document("payments/validation",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        PaymentSnippetFactory.PAYMENT_REQUEST))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(PAYMENT_IMP_UID_DUPLICATED.getMessage()));
    }

    @DisplayName("POST: /payments/validation - 가맹점 주문번호가 중복 됐을 때, - MerchantUid DuplicatedException")
    @Test
    void validatePayment_MerchantUid_DuplicatedException() throws Exception {
        // Given
        PaymentInfo paymentInfo = PaymentFactory.createPaymentInfo(impUid + "7", merchantUid, member);
        PaymentReq paymentReq = PaymentFactory.createPaymentReq(impUid, merchantUid, member.getEmail());
        paymentInfoRepository.save(paymentInfo);
        willDoNothing()
                .given(iamportService)
                .validateIamport(any(String.class), any(BigDecimal.class));

        // When, Then
        mockMvc.perform(post("/payments/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentReq)))
                .andDo(print())
                .andDo(document("payments/validation",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        PaymentSnippetFactory.PAYMENT_REQUEST))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(PAYMENT_MERCHANT_UID_DUPLICATED.getMessage()));
    }
}
