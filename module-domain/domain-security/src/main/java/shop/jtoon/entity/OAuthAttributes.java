package shop.jtoon.entity;

import java.util.Map;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import shop.jtoon.dto.SignUpDto;

@Builder(access = AccessLevel.PRIVATE)
public record OAuthAttributes(
	String nameAttributeKey,
	String email,
	String name,
	String nickname,
	String mobile,
	String gender,
	String password,
	LoginType loginType
) {
	public static OAuthAttributes of(LoginType loginType, String nameAttributeKey, Map<String, Object> attributes) {
		return switch (loginType) {
			case NAVER -> ofNaver(loginType, nameAttributeKey, attributes);
			case KAKAO -> ofKakao(loginType, nameAttributeKey, attributes);
			case LOCAL -> throw new IllegalArgumentException("기본 로그인 정보가 존재합니다.");
		};
	}

	private static OAuthAttributes ofNaver(
		LoginType loginType,
		String nameAttributeKey,
		Map<String, Object> attributes
	) {
		@SuppressWarnings("unchecked") Map<String, Object> response = (Map<String, Object>)attributes.get("response");
		String naverEmail = (String)response.get("email");
		String naverNickname = response.get("nickname") == null ? naverEmail : (String)response.get("nickname");
		String naverPhone = ((String)response.get("mobile")).replace("-", "");

		return OAuthAttributes.builder()
			.nameAttributeKey(nameAttributeKey)
			.email(naverEmail)
			.nickname(naverNickname)
			.mobile(naverPhone)
			.name((String)response.get("name"))
			.gender((String)response.get("gender"))
			.loginType(loginType)
			.build();
	}

	private static OAuthAttributes ofKakao(LoginType loginType, String nameAttributeKey,
		Map<String, Object> attributes) {
		// TODO : KAKAO 추입 도입 예정
		return null;
	}

	public SignUpDto toSignUpDto() {
		return SignUpDto.builder()
			.email(this.email)
			.password(UUID.randomUUID().toString())
			.name(this.name)
			.nickname(this.nickname)
			.gender(this.gender)
			.phone(this.mobile)
			.loginType(this.loginType.name())
			.build();
	}
}
