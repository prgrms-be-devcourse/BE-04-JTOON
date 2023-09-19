package shop.jtoon.dto;

import lombok.Builder;

@Builder
public record LoginDto(
	String email,
	String password
) {
}
