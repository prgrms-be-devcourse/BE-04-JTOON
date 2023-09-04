package com.devtoon.jtoon.paymentinfo.request;

import static com.devtoon.jtoon.global.util.RegExp.*;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CancelReq(
	@NotBlank String impUid,
	@NotBlank String reason,
	@Min(1) int checksum,
	@Pattern(regexp = EMAIL_PATTERN) String buyerEmail,
	@NotBlank @Size(max = 10) String buyerName,
	@Pattern(regexp = PHONE_PATTERN) String buyerPhone
) {
}
