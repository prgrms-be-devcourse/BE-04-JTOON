package com.devtoon.jtoon.paymentinfo.request;

import java.math.BigDecimal;

import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CancelReq(
	@NotBlank String impUid,
	@NotBlank String merchantUid,
	@NotBlank String reason,
	@NotNull @DecimalMin("1") BigDecimal checksum,
	@NotBlank @Size(max = 10) String refundHolder
) {

	public CancelData toCancelData(IamportResponse<Payment> iamportResponse) {
		CancelData cancelData = new CancelData(iamportResponse.getResponse().getImpUid(), true);
		cancelData.setReason(this.reason);
		cancelData.setChecksum(this.checksum);
		cancelData.setRefund_holder(this.refundHolder);

		return cancelData;
	}
}