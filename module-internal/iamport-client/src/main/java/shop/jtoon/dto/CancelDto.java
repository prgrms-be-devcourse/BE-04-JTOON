package shop.jtoon.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CancelDto(
	@NotBlank String impUid,
	@NotBlank String reason,
	@NotNull @DecimalMin("1") BigDecimal checksum,
	@NotBlank @Size(max = 10) String refundHolder
) {
}
