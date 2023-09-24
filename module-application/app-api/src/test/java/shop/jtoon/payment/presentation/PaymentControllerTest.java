package shop.jtoon.payment.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import shop.jtoon.entity.Member;
import shop.jtoon.payment.factory.CreatorFactory;
import shop.jtoon.payment.request.PaymentReq;
import shop.jtoon.repository.MemberRepository;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private MemberRepository memberRepository;

    private String impUid;
    private String merchantUid;
    private String email;
    private Member member;

    @BeforeEach
    void beforeEach() {
        impUid = "impUid123";
        merchantUid = "merchant123";
        email = "example123@naver.com";
        member = CreatorFactory.createMember(email);
    }

    @DisplayName("POST: /payments/validation - 결제 승인 후 결제 정보에 대해 검증 및 생성이 성공적으로 됐을 때, - Amount")
    @Disabled
    @Test
    void validatePayment_Amount() throws Exception {
        // Given
        memberRepository.save(member);
        PaymentReq paymentReq = CreatorFactory.createPaymentReq(impUid, merchantUid, email);

        // When, Then
        mockMvc.perform(post("/payments/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentReq)))
                .andDo(print())
                .andDo(document("payments/validation",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("impUid").type(STRING).description("포트원 결제 고유번호"),
                                fieldWithPath("merchantUid").type(STRING).description("가맹점 주문번호"),
                                fieldWithPath("payMethod").type(STRING).description("결제 방법"),
                                fieldWithPath("itemName").type(STRING).description("결제된 상품명"),
                                fieldWithPath("amount").type(NUMBER).description("결제된 금액"),
                                fieldWithPath("buyerEmail").type(STRING).description("구매자 이메일"),
                                fieldWithPath("buyerName").type(STRING).description("구매자 이름"),
                                fieldWithPath("buyerPhone").type(STRING).description("구매자 전화번호"))))
                .andExpect(status().isCreated())
                .andExpect(content().string(paymentReq.amount().toString()));
    }
}
