package shop.jtoon.payment.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import shop.jtoon.dto.CancelDto;

public record CancelReq(
	@NotBlank String impUid,
	@NotBlank String merchantUid,
	@NotBlank String reason,
	@NotNull @DecimalMin("1") BigDecimal checksum,
	@NotBlank @Size(max = 10) String refundHolder
) {

	public CancelDto toDto() {
		return CancelDto.builder()
			.impUid(this.impUid)
			.reason(this.reason)
			.checksum(this.checksum)
			.refundHolder(this.refundHolder)
			.build();
	}
}
