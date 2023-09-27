package shop.jtoon.factory;

import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

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

    public static final RequestFieldsSnippet CANCEL_REQUEST = requestFields(
            fieldWithPath("impUid").type(STRING).description("포트원 결제 고유번호"),
            fieldWithPath("merchantUid").type(STRING).description("가맹점 주문번호"),
            fieldWithPath("reason").type(STRING).description("환불 사유"),
            fieldWithPath("checksum").type(NUMBER).description("환불 가능 금액"),
            fieldWithPath("refundHolder").type(STRING).description("환불 수령자")
    );

    public static final RequestFieldsSnippet CONDITION_REQUEST = requestFields(
            fieldWithPath("merchantsUid").type(ARRAY).description("가맹점 주문번호 리스트")
    );

    public static final ResponseFieldsSnippet CONDITION_RESPONSE = responseFields(
            fieldWithPath("[].itemName").type(STRING).description("상품명"),
            fieldWithPath("[].itemCount").type(NUMBER).description("상품 수량"),
            fieldWithPath("[].amount").type(NUMBER).description("결제 금액"),
            fieldWithPath("[].createdAt").type(STRING).description("결제 일시")
    );
}
