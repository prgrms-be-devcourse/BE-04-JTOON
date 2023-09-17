package shop.jtoon.payment.request;

import java.util.List;

public record ConditionReq(
	List<String> merchantsUid
) {
}
