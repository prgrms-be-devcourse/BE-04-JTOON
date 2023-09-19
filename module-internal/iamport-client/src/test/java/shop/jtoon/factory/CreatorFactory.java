package shop.jtoon.factory;

import shop.jtoon.dto.CancelDto;

import java.math.BigDecimal;

public class CreatorFactory {

    public static CancelDto createCancelDto(String impUid, int amount) {
        return CancelDto.builder()
                .impUid(impUid)
                .reason("reason")
                .checksum(BigDecimal.valueOf(amount))
                .refundHolder("example@navdr.com")
                .build();
    }
}
