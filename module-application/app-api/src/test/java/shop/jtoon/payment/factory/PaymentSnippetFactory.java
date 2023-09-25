package shop.jtoon.payment.factory;

import org.springframework.restdocs.payload.RequestFieldsSnippet;

import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;

public class PaymentSnippetFactory {
    public static final RequestFieldsSnippet PAYMENT_REQUEST = requestFields(
            fieldWithPath("impUid").type(STRING).description("포트원 결제 고유번호"),
            fieldWithPath("merchantUid").type(STRING).description("가맹점 주문번호"),
            fieldWithPath("payMethod").type(STRING).description("결제 방법"),
            fieldWithPath("itemName").type(STRING).description("결제된 상품명"),
            fieldWithPath("amount").type(NUMBER).description("결제된 금액"),
            fieldWithPath("buyerEmail").type(STRING).description("구매자 이메일"),
            fieldWithPath("buyerName").type(STRING).description("구매자 이름"),
            fieldWithPath("buyerPhone").type(STRING).description("구매자 전화번호")
    );
}
