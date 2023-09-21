package shop.jtoon.payment.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CancelReq(
        @NotBlank String impUid,
        @NotBlank String merchantUid,
        @NotBlank String reason,
        @NotNull @DecimalMin("1") BigDecimal checksum,
        @NotBlank @Size(max = 10) String refundHolder
) {
}
