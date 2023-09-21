package shop.jtoon.payment.request;

import lombok.Builder;

import java.util.List;

@Builder
public record ConditionReq(
        List<String> merchantsUid
) {
}
